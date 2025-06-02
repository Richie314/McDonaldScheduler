package models.station;

import java.lang.reflect.Type;
import java.lang.Thread;
import java.security.InvalidParameterException;

import models.task.Task;

public abstract class Station
extends Thread
{
    public Type producedProduct;
    public Station(Type type)
    {
        this.producedProduct = type;
    }

    /**
     * Adds a Task (of any type) to the station
     * @param task
     */
    public abstract void AddTask(Task task) 
    throws InvalidParameterException;

    public abstract void AddTaskWhenAvaible(Task task) 
    throws InvalidParameterException, InterruptedException;

    /**
     * Does a minumum work.
     * @return the completed task
     */
    public abstract Task DoWork() throws Throwable;

    public void run()
    {
        System.out.println(
            "Station " + 
            getClass().getSimpleName() + 
            " (" + producedProduct.getTypeName() + ") " + 
            "starting..."
        );
        try {
            while (true)
            {
                //Task exceutedTask = 
                this.DoWork();
                //if (exceutedTask == null)
                //{
                //    continue;
                //}
                //notify();
                Thread.sleep(100);
            }
        } catch (Throwable ex) { }
    }
}