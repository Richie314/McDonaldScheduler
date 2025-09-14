package models.scheduler.ordered;

import java.util.Comparator;

import models.station.Station;

public class RandomScheduler
extends OrderedScheduler {
    
    public Comparator<Station> getComparator()
    {
        return Comparator.comparingInt(station -> (int)(Math.random() * 10 * 1000));
    }
}
