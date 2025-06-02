package models.scheduler;

import models.task.Task;
import models.receip.Receip;
import models.station.Station;

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

        if (station.isAlive())
        {
            throw new InvalidParameterException("Station already in use. Cannot add to this scheduler.");
        }

        synchronized (stations)
        {
            stations.add(station);
            station.setDaemon(true);
            station.start();
        }
    }

    public void ShutStations()
    {
        synchronized (stations)
        {
            for (Station station : stations)
            {
                station.interrupt();
            }
            stations.clear();
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
            for (
                Task task = receip.First(receip.StepsCount()); 
                task != null; 
                task = receip.Next(task, receip.StepsCount())
            ) {
                System.out.println(task);
                
                synchronized (task) {
                    Schedule(task);
                    task.wait();
                }

                System.out.println(task);
            }
        } catch (Throwable ex) {
            System.out.println("Exception happended!");
            System.out.println(ex.getMessage());
            var trace = ex.getStackTrace();
            for (int i = 0; i < trace.length; i++)
            {
                System.err.println("\t#" + i + ": " + trace[i]);
            }
        }
    }
}
