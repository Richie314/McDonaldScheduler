package models.Station;

import java.lang.reflect.Type;
import java.lang.Thread;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicInteger;

import models.Task.Task;

public abstract class Station
extends Thread
implements Comparable<Station>
{
    public Type producedProduct;
    private AtomicInteger completedTasks;

    public int completedTasks()
    {
        return this.completedTasks.get();
    }

    public Station(Type type)
    {
        this.completedTasks = new AtomicInteger(0);
        this.producedProduct = type;
    }
    public static boolean Debug = true;

    /**
     * Adds a Task (of any type) to the station
     * @param task
     */
    public abstract void AddTask(Task task) 
    throws InvalidParameterException, IllegalStateException;

    public abstract void AddTaskWhenAvaible(Task task) 
    throws InvalidParameterException, InterruptedException;

    /**
     * Does a minumum work.
     * @return the completed task
     */
    public abstract Task DoWork() throws Throwable;

    /**
     * Returns the number of tasks the station is currently handling
     * @return an int with the task count
     */
    public abstract int TaskCount();

    public void run()
    {
        if (Debug)
        {
            System.out.println(
                "Station " + 
                getClass().getSimpleName() + 
                " (" + producedProduct.getTypeName() + ") " + 
                "starting..."
            );
        }
        try {
            while (true)
            {
                Task exceutedTask = this.DoWork();
                if (exceutedTask != null)
                    this.completedTasks.getAndIncrement();
                //if (exceutedTask == null)
                //{
                //    continue;
                //}
                //notify();
                Thread.sleep(50);
            }
        } catch (Throwable ex) { }
    }

    @Override
    public int compareTo(Station other) {
        return this.producedProduct.toString().compareTo(other.producedProduct.toString());
    }
}