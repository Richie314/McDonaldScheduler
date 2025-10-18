package models.Scheduler.ordered;

import models.Station.Station;

public class RandomScheduler
extends OrderedScheduler {

    public int stationWeight(Station s)
    {
        return (int)(Math.random() * 1000000);
    }
}
