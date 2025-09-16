package models.Scheduler.ordered;

import java.util.Comparator;

import models.Station.Station;

public class WorstFitScheduler
extends OrderedScheduler {

    public Comparator<Station> getComparator()
    {
        // sorts in ascending order by TaskCount()
        return Comparator.comparingInt(station -> station.TaskCount());
    }
}
