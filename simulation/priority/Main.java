package simulation.priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import models.receip.*;
import models.scheduler.Scheduler;
import models.scheduler.greedy.FirstFreeScheduler;
import models.scheduler.greedy.MostFreeScheduler;
import models.scheduler.ordered.RandomScheduler;
import models.scheduler.ordered.RoundRobinScheduler;
import models.station.PriorityStation;
import models.station.Station;

public class Main
{
    private static void complexSim(Scheduler sched, int stationsPerType, long timeout)
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

        Receip longReceip = new ArrayReceip(
            Product1.class, 
            Product2.class, 
            Product3.class,
            Product4.class
        );

        int RECEIP_COUNT = 10;
        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(3 * RECEIP_COUNT);

        for (int i = 0; i < RECEIP_COUNT; i++)
        {
            // Low priority
            threads.add(new Thread(() -> {
                sched.Schedule(longReceip);
                latch.countDown();
            }));

            // Incremental priority
            int priority = 1 + i;
            threads.add(new Thread(() -> {
                sched.Schedule(longReceip, priority);
                latch.countDown();
            }));

            // High priority
            threads.add(new Thread(() -> {
                sched.Schedule(longReceip, RECEIP_COUNT + 10);
                latch.countDown();
            }));
        }

        Collections.shuffle(threads);
        long simStart = System.currentTimeMillis();
        for (Thread thread : threads)
        {
            thread.start();
        }

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
        
        
        for (Thread t : threads)
        {
            if (t.isAlive())
                t.interrupt();
        }
        
    }

    private static void sim1() { complexSim(new FirstFreeScheduler(), 3, 0); }

    private static void sim2() { complexSim(new MostFreeScheduler(), 3, 0); }
    
    private static void sim3() { complexSim(new RoundRobinScheduler(), 3, 0); }

    private static void sim4() { complexSim(new RandomScheduler(), 3, 50); }

    public static void main(String[] args)
    {
        Scheduler.Debug = false;
        Station.Debug = false;
        
        System.out.println("Basic (greedy) scheduling...");
        System.out.println("--------------------------------------");
        sim1();
        System.out.println();
        
        System.out.println("Simple (greedy) scheduling...");
        System.out.println("--------------------------------------");
        sim2();
        System.out.println();

        System.out.println("Round Robin scheduling...");
        System.out.println("--------------------------------------");
        sim3();
        System.out.println();

        System.out.println("Random scheduling...");
        System.out.println("--------------------------------------");
        sim4();
        System.out.println();
    }
}

