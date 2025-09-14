package models.scheduler.ordered;

import java.util.Comparator;

import models.scheduler.Scheduler;
import models.station.Station;
import models.task.Task;

public abstract class OrderedScheduler 
extends Scheduler {

    public abstract Comparator<Station> getComparator();
    
    public void Schedule(Task task)
    throws InterruptedException
    {
        if (task == null)
        {
            return;
        }

        var stationsForThisTask = this.StationsProducing(task.Type);
        if (stationsForThisTask.size() == 0)
        {
            System.err.println("Could not schedule task producing " + task.Type.getTypeName() + "!");
            return;
        }

        do {

            stationsForThisTask.sort(getComparator());
            for (Station station : stationsForThisTask)
            {
                try {
                    station.AddTask(task);
                    return;
                } catch (IllegalStateException ex)
                {
                    // The station has become full or stopped working between the stationsForThisTask.sort()
                    // and the station.AddTask().
                    // In that case we'll check the next one
                }
                // Thread.sleep((long)(Math.random() * 50));
            }

            // System.err.println("Failed cycle to schedule " + task);
            
            // Every station has become unavaible.
            // Let's fetch them again
        } while (true);
    }
}
