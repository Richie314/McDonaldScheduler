package models.Scheduler;

import models.Receip.Receip;
import models.Station.Station;
import models.Task.*;

import java.util.LinkedList;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

public abstract class Scheduler
{
    private LinkedList<Station> stations = new LinkedList<>();

    public void AddStation(Station station) 
        throws InvalidParameterException
    {
        if (station == null)
        {
            throw new InvalidParameterException("Parameter 'station' was null");
        }

        synchronized (stations)
        {
            stations.add(station);
        }
    }

    protected LinkedList<Station> StationsProducing(Type product)
    {
        LinkedList<Station> filtered = new LinkedList<>();
        synchronized (stations)
        {
            for (var s : stations)
            {
                if (s.producedProduct == product)
                {
                    filtered.add(s);
                }
            }
        }
        return filtered;
    }

    public abstract void Schedule(Task task);

    public void Schedule(Receip receip)
    {
        try {
            for (Task task = receip.First(); task != null; task = receip.Next(task))
            {
                System.out.println("Task: " + task.GetSignature());
            }
        } catch (Throwable ex) {
            System.out.println("Exception happended!");
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
        }
    }
}
