package simulation.simple;

import models.Recipe;
import models.Scheduler.Scheduler;
import models.Scheduler.greedy.FirstStationScheduler;
import models.Station.FIFOStation;
import models.Station.Station;

public class Main
{
    /**
     * This is a 2 products simulation.
     * It is used only for testing if the methods work and not how efficient they are
     */
    public static void main(String[] args)
    {
        System.out.println("Running two products simulation...");
        System.out.println("--------------------------------------");
        System.out.println();

        // These are the stations producing the products from nothing or from something else
        Station s1 = new FIFOStation(Product1.class, 5);
        Station s2 = new FIFOStation(Product1.class, 3);
        Station s3 = new FIFOStation(Product2.class, 5);

        // This is the scheduler we'll use to route Tasks to Stations
        Scheduler sched = new FirstStationScheduler();
        sched.AddStation(s1);
        sched.AddStation(s2);
        sched.AddStation(s3);

        // This represents the following series of steps:
        // null -> Product1 -> Product2
        Recipe r = new Recipe(Product1.class, Product2.class);
        r.SendToScheduler(sched);
        sched.ShutStations();
        
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Simulation ended");
    }
}
