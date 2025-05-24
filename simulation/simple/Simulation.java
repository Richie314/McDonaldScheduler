package simulation.simple;

import models.Receip.Receip;
import models.Receip.TwoStepReceip;
import models.Scheduler.FirstFreeScheduler;
import models.Scheduler.Scheduler;
import models.Station.FIFOStation;
import models.Station.Station;

public class Simulation
{
    /**
     * This is a 2 products simulation
     * Is used only for testing if the methods work and not how efficient they are
     */
    public static void Main(String[] args)
    {
        System.out.println("Running two products simulation...");

        // These are the stations producing the products from nothing or from something else
        Station s1 = new FIFOStation(Product1.class, 5);
        Station s2 = new FIFOStation(Product1.class, 3);
        Station s3 = new FIFOStation(Product1.class, 5);

        // This is the scheduler we'll use to route Tasks to Stations
        Scheduler sched = new FirstFreeScheduler();
        sched.AddStation(s1);
        sched.AddStation(s2);
        sched.AddStation(s3);

        // This represents the following series of steps:
        // null -> Product1 -> Product2
        Receip r = new TwoStepReceip(Product1.class, Product2.class);
        sched.Schedule(r);
    }
}
