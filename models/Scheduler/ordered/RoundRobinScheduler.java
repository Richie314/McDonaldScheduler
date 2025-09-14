package models.scheduler.ordered;

import java.util.Comparator;

import models.station.Station;

public class RoundRobinScheduler
extends OrderedScheduler {

    public Comparator<Station> getComparator()
    {
        return Comparator.comparingInt(station -> station.TaskCount());
    }
}
