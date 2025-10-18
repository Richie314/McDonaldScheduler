package models.Scheduler.ordered;

import models.Station.Station;

public class BestFitScheduler
extends OrderedScheduler {
    
    public int stationWeight(Station s)
    {
        return -s.TaskCount();
    }
}
