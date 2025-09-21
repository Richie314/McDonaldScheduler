package models.Station;

import java.security.InvalidParameterException;
import java.util.Queue;
import java.util.ArrayDeque;
import java.lang.reflect.Type;

import models.Task.Task;

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

    public void AddTask(Task task)
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
            queue.notifyAll();
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
                    queue.notifyAll();
                    return;
                }
                queue.wait();
            }
        } while (true);
    }

    public Task GetNextTask() throws Throwable
    {
        Task task = null;

        do {
            synchronized (queue)
            {
                task = queue.poll();
                if (task != null)
                {
                    queue.notifyAll();
                } else {
                    queue.wait();
                }
            }
        } while (task == null);
        
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
