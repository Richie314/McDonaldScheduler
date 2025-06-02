package models.task;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementalIdentifier
{
    private int id;
    private static final AtomicInteger counter = new AtomicInteger();

    public IncrementalIdentifier()
    {
        id = counter.incrementAndGet();
    }

    public int getId()
    {
        return this.id;
    }
}
