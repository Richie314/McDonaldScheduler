package models.station;

import models.task.Task;
import java.security.InvalidParameterException;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.reflect.Type;

public class FIFOStation
extends Station
{
    private int capacity;
    private Queue<Task> queue = new LinkedList<>();

    public FIFOStation(Type type, int capacity)
    {
        super(type);
        if (capacity <= 0)
        {
            throw new InvalidParameterException("Parameter 'capacity' was <= 0");
        }
        this.capacity = capacity;
    }

    public synchronized void AddTask(Task task)
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

        queue.offer(task);
    }

    public void AddTaskWhenAvaible(Task task)
    throws InvalidParameterException, InterruptedException
    {
        if (task == null)
        {
            throw new InvalidParameterException("Parameter 'task' was null");
        }

        if (task.Type != producedProduct)
        {
            throw new InvalidParameterException(
                "Cannot add a task that produces " + 
                task.Type.getTypeName() + 
                " to a station designed for " + 
                producedProduct.getTypeName()
            );
        }

        while (!isAlive())
        {
            wait(500);
        }
        synchronized (queue)
        {
            if ((queue.size() < capacity))
            {
                queue.offer(task);
                return;
            }
        }
    }

    public Task DoWork() throws Throwable
    {
        Task task = null;
        synchronized (queue)
        {
            task = queue.poll();
        }

        if (task == null)
        {
            return null;
        }

        task.DoWork();
        return task;
    }
}
