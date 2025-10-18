package models.Scheduler.greedy;

import models.Scheduler.Scheduler;
import models.Task.Task;

/**
 * A scheduer that enques the item in the first station found.
 * Waits for it to free itself if needed.
 */
public class FirstStationScheduler 
extends Scheduler
{
    public void Schedule(Task task)
    {
        if (task == null)
        {
            return;
        }
        
        for (var station : stations)
        {
            if (station.producedProduct != task.Type)
                continue;

            try {
                station.AddTaskWhenAvaible(task);
                return;
            } catch (InterruptedException ex) { }
        }
    }
}
