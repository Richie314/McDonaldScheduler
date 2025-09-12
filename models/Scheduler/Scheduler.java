package models.scheduler;

import models.task.Task;
import models.receip.Receip;
import models.station.Station;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

public abstract class Scheduler
{
    public static boolean Debug = true;

    protected List<Station> stations = new ArrayList<>();

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

    protected List<Station> StationsProducing(Type product)
    {
        return stations
            .stream()
            .filter(s -> s.producedProduct == product)
            .collect(Collectors.toList());
    }

    public abstract void Schedule(Task task) throws InterruptedException;

    public void Schedule(Receip receip, int priority)
    {
        try {
            for (
                Task task = receip.First(receip.StepsCount(), priority); 
                task != null; 
                task = receip.Next(task, receip.StepsCount(), priority)
            ) {
                if (Debug)
                {
                    System.out.println(task);
                }
                
                synchronized (task) {
                    Schedule(task);
                    task.wait();
                }

                //System.out.println(task);
            }
        } catch (Throwable ex) {
            System.err.println("Exception happended!");
            System.err.println(ex.getMessage());
            var trace = ex.getStackTrace();
            for (int i = 0; i < trace.length; i++)
            {
                System.err.println("\t#" + i + ": " + trace[i]);
            }
        }
    }

    public void Schedule(Receip receip)
    {
        Schedule(receip, 0);
    }
}
