package models.Scheduler.greedy;

import java.util.Comparator;

import models.Scheduler.Scheduler;
import models.Station.FIFOStation;
import models.Station.Station;
import models.Task.Task;

public class MostFreeScheduler
extends Scheduler
{
    public void Schedule(Task task)
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
        stationsForThisTask.sort(getComparator());
        
        for (var station : stationsForThisTask)
        {
            try {
                station.AddTaskWhenAvaible(task);
                return;
            } catch (InterruptedException ex) { }
        }
    }

    private static Comparator<Station> getComparator()
    {
        return Comparator.comparingDouble(station -> {
            if (station instanceof FIFOStation)
            {
                return ((FIFOStation)station).fillingStatus();
            }

            return 0.5; // Assume every other station is always half-filled
        });
    }
}

