package models.Station;

import java.lang.reflect.Type;
import java.lang.Thread;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import models.Task.Task;

public abstract class Station
extends Thread
implements Comparable<Station>
{
    public Type producedProduct;
    private final AtomicInteger completedTasks;

    private AtomicBoolean isExecuting = new AtomicBoolean(false);
    protected int tasksInExecution()
    {
        return isExecuting.get() ? 1 : 0;
    }

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
     * Fetches the next Task to execute
     * @return the task to be executed
     */
    public abstract Task GetNextTask() throws Throwable;

    /**
     * Returns the number of tasks the station is currently handling
     * @return an int with the task count
     */
    public abstract int TaskCount();

    /**
     * Returns the number of tasks the station is currently handling 
     * over the maximum nuber the station can handle
     * @return an int with the task count
     */
    public abstract double fillingStatus();

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
            while (!Thread.currentThread().isInterrupted())
            {
                Task task = this.GetNextTask();
                if (task == null)
                    break;
                
                synchronized (task) {

                    isExecuting.set(true);
                    task.DoWork();
                    isExecuting.set(false);

                    this.completedTasks.getAndIncrement();
                    task.notify(); // Wake the task.wait() inside Receip class
                }
            }
        } catch (Throwable ex) { }
    }

    @Override
    public int compareTo(Station other) {
        return this.producedProduct.toString().compareTo(other.producedProduct.toString());
    }
}