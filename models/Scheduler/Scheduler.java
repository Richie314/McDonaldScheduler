package models.Scheduler;

import models.Receip.Receip;
import models.Station.Station;
import models.Task.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

public abstract class Scheduler
{
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

        stations.add(station);
        station.start();
    }

    public void ShutStations()
    {
        stations.stream().forEach(station -> station.interrupt());
        stations.clear();
    }

    protected List<Station> StationsProducing(Type product)
    {
        return stations
            .stream()
            .filter(s -> s.producedProduct == product)
            .collect(Collectors.toList());
    }

    public abstract void Schedule(Task task) throws InterruptedException;
}
