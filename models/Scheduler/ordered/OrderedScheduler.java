package models.Scheduler.ordered;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Comparator;
//import java.util.Map;
//import java.util.stream.Collectors;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

import models.Scheduler.Scheduler;
import models.Station.Station;
import models.Task.Task;

public abstract class OrderedScheduler 
extends Scheduler {

    private Dictionary<Type, ArrayList<Station>> stationsGrouped = new Hashtable<Type, ArrayList<Station>>();

    public void AddStation(Station station)
        throws InvalidParameterException
    {
        super.AddStation(station);

        if (stationsGrouped.get(station.producedProduct) == null)
        {
            stationsGrouped.put(station.producedProduct, new ArrayList<>());
        }
        stationsGrouped.get(station.producedProduct).add(station);
    }

    public abstract int stationWeight(Station station);
    
    public void Schedule(Task task)
    throws InterruptedException
    {
        if (task == null)
        {
            return;
        }

        var stationsForThisTask = stationsGrouped.get(task.Type);
        if (stationsForThisTask == null)
        {
            System.err.println("Could not schedule task producing " + task.Type.getTypeName() + "!");
            return;
        }

        do {

            // Stations ordered according to stationWeight() method.
            // stationWeight() is called only once per station.
            // This approach reduces locks but has an overall worse performance.
            /*
            var stationsToCheck = stationsForThisTask
                .stream()
                .collect(Collectors.toMap(s -> s, s -> stationWeight(s)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
            */

            // Sort according to the stationWeight() method
            // stationWeight() is called around O(n logn) times
            // Despite those many calls, this approach seems faster.
            var stationsToCheck = new ArrayList<>(stationsForThisTask);
            stationsToCheck.sort(Comparator.comparingInt(s -> stationWeight(s)));

            for (var station : stationsToCheck)
            {
                try {
                    station.AddTask(task);
                    return;
                } catch (IllegalStateException ex)
                {
                    // The station has become full or stopped working between the sorting
                    // and the station.AddTask().
                    // In that case we'll check the next one
                }
            }
            
            // Every station has become unavaible.
            // Let's fetch them again
        } while (true);
    }
}
