package models.Station;

import java.security.InvalidParameterException;
import java.util.Queue;
import java.lang.reflect.Type;
import models.Task.Task;

public class FIFOStation
extends Station
{
    private int capacity;
    private Queue<Task> queue;

    public FIFOStation(Type type, int capacity)
    {
        super(type);
        if (capacity <= 0)
        {
            throw new InvalidParameterException("Parameter 'capacity' was <= 0");
        }
        this.capacity = capacity;
    }

    public void AddTask(Task task)
    {
        if (task == null)
        {
            throw new InvalidParameterException("Parameter 'task' was null");
        }

        if (task.Type != producedProduct)
        {
            throw new InvalidParameterException(
                "Cannot add a task that produces " + task.Type.getTypeName() + " to a station designed for " + producedProduct.getTypeName());
        }

        synchronized(queue)
        {
            queue.add(task);
        }
    }

    public synchronized boolean AddTaskIfAvaible(Task task)
    {
        if (task == null || task.Type != producedProduct)
        {
            return false;
        }

        if (queue.size() >= capacity)
        {
            return false;
        }
        queue.add(task);
        return true;
    }

    public Task DoWork() throws Throwable
    {
        Task task = null;

        synchronized (queue)
        {
            if (!queue.isEmpty())
            {
                task = queue.remove();
            }
        }

        if (task == null)
        {
            return null;
        }

        task.DoWork();
        return task;
    }

    public Task[] ParallelWork() throws Exception
    {
        throw new Exception("Feature not implemented");
    }

    public synchronized boolean IsAvaible()
    {
        return queue.size() < capacity;
    }
}
