package simulation.priority;

import models.receip.*;
import models.scheduler.FirstFreeScheduler;
import models.scheduler.Scheduler;
import models.station.PriorityStation;
import models.station.Station;

public class Main
{
    private static void sim1()
    {
        Station s1 = new PriorityStation(Product1.class, 3);

        Scheduler sched = new FirstFreeScheduler();
        sched.AddStation(s1);

        Receip shortReceip = new SingleStepReceip(Product1.class);

        int RECEIP_COUNT = 10;

        Thread[] threads = new Thread[] {
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(shortReceip);
                }
                System.out.println("Low priority (short) receips ended");
            }),
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(shortReceip, i + 1);
                }
                System.out.println("Incremental priority (short) receips ended");
            }),
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(shortReceip, RECEIP_COUNT+10);
                }
                System.out.println("High priority (short) receips ended");
            })
        };

        for (Thread thread : threads)
        {
            thread.start();
        }
        for (Thread thread : threads)
        {
            try {
                thread.join();
            } catch (InterruptedException ex) {}
        }
    }
    
    private static void sim2()
    {
        Station s1 = new PriorityStation(Product1.class, 3);
        Station s2 = new PriorityStation(Product2.class, 3);
        Station s3 = new PriorityStation(Product3.class, 3);
        Station s4 = new PriorityStation(Product4.class, 3);

        Scheduler sched = new FirstFreeScheduler();
        sched.AddStation(s1);
        sched.AddStation(s2);
        sched.AddStation(s3);
        sched.AddStation(s4);

        Receip longReceip = new ArrayReceip(
            Product1.class, 
            Product2.class, 
            Product3.class,
            Product4.class
        );

        int RECEIP_COUNT = 10;

        Thread[] threads = new Thread[] {
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(longReceip);
                }
                System.out.println("Low priority (long) receips ended");
            }),
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(longReceip, i + 1);
                }
                System.out.println("Incremental priority (long) receips ended");
            }),
            new Thread(() -> {
                for (int i = 0; i < RECEIP_COUNT; i++)
                {
                    sched.Schedule(longReceip, RECEIP_COUNT+10);
                }
                System.out.println("High priority (long) receips ended");
            })
        };

        for (Thread thread : threads)
        {
            thread.start();
        }
        for (Thread thread : threads)
        {
            try {
                thread.join();
            } catch (InterruptedException ex) {}
        }
    }
    public static void main(String[] args)
    {
        Scheduler.Debug = false;
        Station.Debug = false;

        System.out.println("Running one product simulation...");
        System.out.println("--------------------------------------");
        sim1();
        System.out.println();
        
        System.out.println("Running four products simulation...");
        System.out.println("--------------------------------------");
        sim2();
        System.out.println();
    }
}

