package simulation.multiple_receips;

import java.util.function.Function;

import models.receip.*;
import models.scheduler.FirstFreeScheduler;
import models.scheduler.Scheduler;
import models.station.FIFOStation;
import models.station.Station;

public class Main
{
    /**
     * This is a 5 products, 2 receips simulation
     * Is used only for testing if the methods work and not how efficient they are
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
            new SingleStepReceip(Product1.class),
            new SingleStepReceip(Product3.class),
            new SingleStepReceip(Product4.class),
            new SingleStepReceip(Product5.class),

            new TwoStepReceip(Product1.class, Product2.class),
            new TwoStepReceip(Product3.class, Product4.class),
            new TwoStepReceip(Product4.class, Product5.class),
            
            new ArrayReceip(Product3.class, Product4.class, Product5.class),
        };

        Function<Integer, Runnable> schedule = receipsCount -> {
            return () -> {
                for (int i = 0; i < receipsCount; i++)
                {
                    Receip receip = receips[(int) Math.floor(Math.random() * receips.length)];
                    sched.Schedule(receip);
                }
                System.out.println(receipsCount + " receips completed");
            };
        };

        // Simulating requests from 3 different sources
        new Thread(schedule.apply(9)).start();
        new Thread(schedule.apply(7)).start();
        new Thread(schedule.apply(13)).start();
    }
}
