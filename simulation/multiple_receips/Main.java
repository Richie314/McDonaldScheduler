package simulation.multiple_receips;

import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;

import models.Receip.Receip;
import models.Scheduler.Scheduler;
import models.Scheduler.greedy.FirstFreeScheduler;
import models.Station.FIFOStation;
import models.Station.Station;

public class Main
{
    /**
     * This is a 5 products, 2 receips simulation.
     * It is used only for testing if the methods work and not how efficient they are
     */
    public static void main(String[] args)
    {
        System.out.println("Running five products simulation...");
        System.out.println("--------------------------------------");
        System.out.println();

        Station s1 = new FIFOStation(Product1.class, 1);
        Station s2 = new FIFOStation(Product2.class,1);
        Station s3 = new FIFOStation(Product3.class, 1);
        Station s4 = new FIFOStation(Product4.class, 1);
        Station s5 = new FIFOStation(Product5.class, 1);

        Scheduler sched = new FirstFreeScheduler();
        sched.AddStation(s1);
        sched.AddStation(s2);
        sched.AddStation(s3);
        sched.AddStation(s4);
        sched.AddStation(s5);

        Receip[] receips = {
            new Receip(Product1.class),
            new Receip(Product3.class),
            new Receip(Product4.class),
            new Receip(Product5.class),

            new Receip(Product1.class, Product2.class),
            new Receip(Product3.class, Product4.class),
            new Receip(Product4.class, Product5.class),
            
            new Receip(Product3.class, Product4.class, Product5.class),
        };

        Function<Integer, Runnable> schedule = receipsCount -> {
            return () -> {
                for (int i = 0; i < receipsCount; i++)
                {
                    Receip receip = receips[(int) Math.floor(Math.random() * receips.length)];
                    receip.SendToScheduler(sched);
                }
                System.out.println(receipsCount + " receips completed");
            };
        };

        // Simulating requests from 3 different sources
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(schedule.apply(9)));
        threads.add(new Thread(schedule.apply(7)));
        threads.add(new Thread(schedule.apply(13)));
        threads.stream().forEach(t -> t.start());

        threads.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        });
        sched.ShutStations();
    }
}
