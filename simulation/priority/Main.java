package simulation.priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import models.Recipe;
import models.Scheduler.Scheduler;
import models.Scheduler.complex.RoundRobinScheduler;
import models.Scheduler.greedy.FirstStationScheduler;
import models.Scheduler.greedy.MostFreeScheduler;
import models.Scheduler.ordered.BestFitScheduler;
import models.Scheduler.ordered.FirstFitScheduler;
import models.Scheduler.ordered.RandomScheduler;
import models.Scheduler.ordered.WorstFitScheduler;
import models.Station.PriorityStation;
import models.Station.Station;

public class Main
{
    private static void sim(Scheduler sched, int stationsPerType, long timeout)
    {
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < stationsPerType; i++)
        {
            stations.add(new PriorityStation(Product1.class, 5));
            stations.add(new PriorityStation(Product2.class, 5));
            stations.add(new PriorityStation(Product3.class, 5));
            stations.add(new PriorityStation(Product4.class, 5));
        }

        stations.sort(Comparator.naturalOrder());
        for (var station : stations)
        {
            sched.AddStation(station);
        }

        Recipe longRecipe = new Recipe(
            Product1.class, 
            Product2.class, 
            Product3.class,
            Product4.class
        );

        int RECIPE_COUNT = 10;
        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(3 * RECIPE_COUNT);

        for (int i = 0; i < RECIPE_COUNT; i++)
        {
            // Low priority
            threads.add(new Thread(() -> {
                longRecipe.SendToScheduler(sched);
                latch.countDown();
            }));

            // Incremental priority
            int priority = 1 + i;
            threads.add(new Thread(() -> {
                longRecipe.SendToScheduler(sched, priority);
                latch.countDown();
            }));

            // High priority
            threads.add(new Thread(() -> {
                longRecipe.SendToScheduler(sched, RECIPE_COUNT + 10);
                latch.countDown();
            }));
        }

        Collections.shuffle(threads);
        long simStart = System.currentTimeMillis();
        threads.stream().forEach(t -> t.start());

        try {
            if (timeout > 0)
                latch.await(timeout, TimeUnit.SECONDS);
            else
                latch.await();
        } catch (InterruptedException ex) {
            System.err.println("Simulation stopped.");
        }

        float duration = (float)(System.currentTimeMillis() - simStart) / 1000;
        System.out.println("Simulation ended in " + duration + " s");
        for (var station : stations)
        {
            System.out.println("\t" + station.producedProduct.getTypeName() + ": " + station.completedTasks());
        }
        
        sched.ShutStations();
    }

    public static void main(String[] args)
    {
        Recipe.Debug = false;
        Station.Debug = false;
        
        System.out.println("Basic (greedy) scheduling...");
        System.out.println("--------------------------------------");
        sim(new FirstStationScheduler(), 3, 0);
        System.out.println();
        
        System.out.println("Simple (greedy) scheduling...");
        System.out.println("--------------------------------------");
        sim(new MostFreeScheduler(), 3, 0);
        System.out.println();

        System.out.println("Random scheduling...");
        System.out.println("--------------------------------------");
        sim(new RandomScheduler(), 3, 50);
        System.out.println();
        
        System.out.println("First-fit scheduling...");
        System.out.println("--------------------------------------");
        sim(new FirstFitScheduler(), 3, 0);
        System.out.println();

        System.out.println("Best-fit scheduling...");
        System.out.println("--------------------------------------");
        sim(new BestFitScheduler(), 3, 0);
        System.out.println();

        System.out.println("Worst-fit scheduling...");
        System.out.println("--------------------------------------");
        sim(new WorstFitScheduler(), 3, 0);
        System.out.println();

        System.out.println("Round Robin scheduling...");
        System.out.println("--------------------------------------");
        sim(new RoundRobinScheduler(), 3, 0);
        System.out.println();
    }
}

