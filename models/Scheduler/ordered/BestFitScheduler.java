package models.Scheduler.ordered;

import java.util.Comparator;

import models.Station.Station;

public class BestFitScheduler
extends OrderedScheduler {

    public Comparator<Station> getComparator()
    {
        // sorts in descending order by TaskCount()
        return Comparator.comparingInt(station -> -station.TaskCount());
    }
}
