package models.Station;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.reflect.Type;

import models.Task.PriorityTask;

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
}
