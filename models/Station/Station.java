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
    public abstract void AddTask(Task task) throws InvalidParameterException;

    public abstract boolean AddTaskIfAvaible(Task task);

    public /*synchronized*/ void AddTaskWhenAvaible(Task task) throws InterruptedException
    {
        while (!AddTaskIfAvaible(task))
        {
            wait();
        }
    }

    /**
     * Does a minumum work.
     * @return the completed task
     */
    public abstract Task DoWork() throws Throwable;

    /**
     * Does a minumum work.
     * If supported, executes one or more Tasks, returning all of them
     * @return
     */
    public abstract Task[] ParallelWork() throws Exception;

    public abstract boolean IsAvaible();

    public boolean IsFull() { return !this.IsAvaible(); }

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
                Task exceutedTask = this.DoWork();
                if (exceutedTask == null)
                {
                    //wait(100);
                    continue;
                }
                notifyAll();
            }
        } catch (Throwable ex) { }
    }

    public void ShutDown()
    {
        this.interrupt();
    }
}