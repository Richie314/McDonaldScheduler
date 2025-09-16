package models.Task;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementalIdentifier
{
    private int id;
    private static final AtomicInteger lastId = new AtomicInteger(1);

    public static int reserveIdRange(int length)
    {
        return lastId.getAndAdd(length);
    }

    public synchronized int getId()
    {
        return id;
    }
    public synchronized void setId(int newId)
    {
        id = newId;
    }
}
