package models.scheduler;

import java.util.Comparator;

import models.station.Station;
import models.task.Task;

public abstract class OrderedScheduler 
extends Scheduler {

    public abstract Comparator<Station> getComparator();
    
    public synchronized void Schedule(Task task)
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
            }

            // Every station has become unavaible.
            // Let's fetch them again
        } while (true);
    }
}
