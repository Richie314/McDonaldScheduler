package simulation.big;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import models.Receip.Receip;
import models.Receip.TwoStepReceip;
import models.Receip.ArrayReceip;
import models.Scheduler.Scheduler;
import models.Scheduler.complex.RoundRobinScheduler;
import models.Scheduler.ordered.BestFitScheduler;
import models.Scheduler.ordered.FirstFitScheduler;
import models.Scheduler.ordered.RandomScheduler;
import models.Scheduler.ordered.WorstFitScheduler;
import models.Station.PriorityStation;
import models.Station.Station;

public class Main
{
    public static final int RECEIP_COUNT = 80;
    private static void sim(Scheduler sched, int stationsPerType, long timeout)
    {
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < stationsPerType; i++)
        {
            stations.add(new PriorityStation(ProductA1.class, 5));
            stations.add(new PriorityStation(ProductA2.class, 5));
            stations.add(new PriorityStation(ProductA3.class, 5));
            stations.add(new PriorityStation(ProductA4.class, 5));
            
            stations.add(new PriorityStation(ProductB1.class, 4));
            stations.add(new PriorityStation(ProductB2.class, 4));
            stations.add(new PriorityStation(ProductB3.class, 4));
        }

        stations.sort(Comparator.naturalOrder());
        for (var station : stations)
        {
            sched.AddStation(station);
        }

        Receip[] receips = new Receip[] {
            new ArrayReceip(
            ProductA1.class, 
                ProductA2.class, 
                ProductA3.class,
                ProductA4.class
            ),
            new TwoStepReceip(ProductA1.class, ProductA2.class),
            new ArrayReceip(
            ProductB1.class,
                ProductB2.class,
                ProductB3.class
            ),
            new TwoStepReceip(ProductB1.class, ProductB2.class)
        };

        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(3 * RECEIP_COUNT);

        Random rand = new Random(1);

        for (int i = 0; i < RECEIP_COUNT; i++)
        {
            // Low priority
            threads.add(new Thread(() -> {
                Receip receip = receips[rand.nextInt(receips.length)];
                receip.SendToScheduler(sched);
                latch.countDown();
            }));

            // Incremental priority
            int priority = 1 + i;
            threads.add(new Thread(() -> {
                Receip receip = receips[rand.nextInt(receips.length)];
                receip.SendToScheduler(sched, priority);
                latch.countDown();
            }));

            // High priority
            threads.add(new Thread(() -> {
                Receip receip = receips[rand.nextInt(receips.length)];
                receip.SendToScheduler(sched, RECEIP_COUNT + 10);
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

        Map<String, List<Station>> stationByProducedType = stations
            .stream()
            .collect(Collectors.groupingBy(s -> s.producedProduct.getTypeName()))
        ;

        stationByProducedType
            .keySet()
            .stream()
            .sorted()    
            .forEach(typeName -> {
                List<String> usage = stationByProducedType.get(typeName)
                    .stream()
                    .map(s -> String.valueOf(s.completedTasks()))
                    .toList()
                ;
                System.out.println("\t" + typeName + ": " + String.join("-", usage));
            })
        ;
        
        sched.ShutStations();
    }

    public static void main(String[] args)
    {
        Receip.Debug = false;
        Station.Debug = false;

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

