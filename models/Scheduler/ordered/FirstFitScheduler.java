package models.Scheduler.ordered;

import java.util.Comparator;

import models.Station.Station;

public class FirstFitScheduler
extends OrderedScheduler {

    public Comparator<Station> getComparator()
    {
        // No sorting is done: first avaible is taken
        return Comparator.comparingInt(s -> 1);
    }
}
