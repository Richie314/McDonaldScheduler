package models.Station;

import java.util.PriorityQueue;

import models.Task.*;

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
}
