package models.station;

import models.task.*;
import java.util.PriorityQueue;
import java.lang.reflect.Type;
import java.util.Comparator;

public class PriorityStation
extends FIFOStation
{  
    public PriorityStation(Type type, int capacity)
    {
        super(type, capacity);
        queue = new PriorityQueue<>(Comparator.comparingInt(
            task -> {
                if (task instanceof PriorityTask)
                {
                    return ((PriorityTask)task).Priority;
                }
                return 0;
            }).reversed()
        );    
    }

    /*
    public void AddTask(Task task)
    {
        if (!(task instanceof PriorityTask))
        {
            throw new InvalidParameterException("Task priority cannot be deduced");
        }
        super.AddTask(task);
    }

    public void AddTaskWhenAvaible(Task task)
    throws InvalidParameterException, InterruptedException
    {
        if (!(task instanceof PriorityTask))
        {
            throw new InvalidParameterException("Task priority cannot be deduced");
        }
        super.AddTaskWhenAvaible(task);
    }
    */
}
