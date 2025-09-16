package models.Scheduler.greedy;

import models.Scheduler.Scheduler;
import models.Task.Task;

/**
 * A scheduer that enques the item in the first free station
 */
public class FirstFreeScheduler 
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
        
        for (var station : stationsForThisTask)
        {
            try {
                station.AddTaskWhenAvaible(task);
                return;
            } catch (InterruptedException ex) { }
        }
    }
}
