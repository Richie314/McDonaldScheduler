package models.Scheduler;

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
            return;
        }
        while (true)
        {
            for (var station : stationsForThisTask)
            {
                if (station.AddTaskIfAvaible(task))
                {
                    return;
                }
                //if (station.IsAvaible())
                //{
                //    station.AddTask(task);
                //    return;
                //}
            }
        }
    }
}
