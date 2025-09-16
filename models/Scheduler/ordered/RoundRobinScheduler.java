package models.Scheduler.ordered;

import java.util.Comparator;

import models.Station.Station;

public class RoundRobinScheduler
extends OrderedScheduler {

    public Comparator<Station> getComparator()
    {
        return Comparator.comparingInt(station -> station.TaskCount());
    }
}
