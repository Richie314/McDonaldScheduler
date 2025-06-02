package simulation.multiple_receips;

import models.receip.*;
import models.scheduler.FirstFreeScheduler;
import models.scheduler.Scheduler;
import models.station.FIFOStation;
import models.station.Station;
import java.lang.reflect.Type;

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
        Station s2 = new FIFOStation(Product2.class, 1);
        Station s3 = new FIFOStation(Product3.class, 1);
        Station s4 = new FIFOStation(Product4.class, 1);
        Station s5 = new FIFOStation(Product5.class, 1);

        Scheduler sched = new FirstFreeScheduler();
        sched.AddStation(s1);
        sched.AddStation(s2);
        sched.AddStation(s3);
        sched.AddStation(s4);
        sched.AddStation(s5);

        Receip r1 = new TwoStepReceip(Product1.class, Product2.class);
        Receip r2 = new SingleStepReceip(Product3.class);
        Receip r3 = new SingleStepReceip(Product4.class);
        Receip r4 = new SingleStepReceip(Product5.class);
        Receip r5 = new TwoStepReceip(Product4.class, Product5.class);
        Receip r6 = new ArrayReceip(Product3.class, Product4.class, Product5.class);

        sched.Schedule(r1);
        sched.Schedule(r2);
        sched.Schedule(r3);
        sched.Schedule(r4);
        sched.Schedule(r5);
        sched.Schedule(r6);
    }
}
