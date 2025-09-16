package models.Scheduler.complex;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Dictionary;
import java.util.Hashtable;

import models.Scheduler.Scheduler;
import models.Station.Station;
import models.Task.Task;

public class RoundRobinScheduler
extends Scheduler {

    private Dictionary<Type, StationGroup> groupedStations = new Hashtable<>();

    @Override
    public void AddStation(Station station) throws InvalidParameterException
    {
        super.AddStation(station);

        var group = groupedStations.get(station.producedProduct);
        if (group == null)
        {
            group = new StationGroup();
            groupedStations.put(station.producedProduct, group);
        }

        groupedStations.get(station.producedProduct).Add(station);
    }

    public void Schedule(Task task)
    throws InterruptedException
    {
        if (task == null)
        {
            return;
        }

        var stationsGroup = groupedStations.get(task.Type);
        if (stationsGroup == null)
        {
            System.err.println("Could not schedule task producing " + task.Type.getTypeName() + "!");
            return;
        }

        Station chosenStation = stationsGroup.getNext();
        chosenStation.AddTaskWhenAvaible(task);
    }
}
