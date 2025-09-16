package models.Scheduler.complex;

import models.Station.Station;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StationGroup {
    private List<Station> stations = new ArrayList<>();
    private AtomicInteger currentIndex = new AtomicInteger();

    public void Add(Station station)
    {
        stations.add(station);
    }

    public Station getNext()
    {
        int index = currentIndex.getAndIncrement();
        return stations.get(index % stations.size());
    }
}
