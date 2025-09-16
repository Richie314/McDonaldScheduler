package models.Station;

import java.security.InvalidParameterException;
import java.util.Queue;

import models.Task.Task;

import java.util.ArrayDeque;
import java.lang.reflect.Type;

public class FIFOStation
extends Station
{
    private int capacity;
    protected Queue<Task> queue;

    public FIFOStation(Type type, int capacity)
    {
        super(type);
        if (capacity <= 0)
        {
            throw new InvalidParameterException("Parameter 'capacity' was <= 0");
        }
        this.capacity = capacity;
        queue = new ArrayDeque<>(capacity);
    }

    public /*synchronized*/ void AddTask(Task task)
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

        if (!isAlive())
        {
            throw new IllegalStateException("Cannot add task now: station is not running!");
        }

        synchronized (queue)
        {
            if (queue.size() >= capacity)
            {
                throw new IllegalStateException("Cannot add task now: station is full!");
            }

            queue.offer(task);
        }
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
            Thread.sleep(500);
        }
        do {
            synchronized (queue)
            {
                if (queue.size() < capacity)
                {
                    queue.offer(task);
                    return;
                }
            }
            Thread.sleep(500);
        } while (true);
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

    public int TaskCount()
    {
        synchronized (queue)
        {
            return queue.size();
        }
    }

    public double fillingStatus()
    {
        return TaskCount() / (double)capacity;
    }
}
