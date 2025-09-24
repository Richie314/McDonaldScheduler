package models;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Dictionary;
import java.util.Hashtable;

import models.Task.PriorityTask;
import models.Task.Task;
import models.Scheduler.Scheduler;

public class Recipe
{
    public static boolean Debug = true;
    
    private Dictionary<Type, Type> Steps = new Hashtable<Type, Type>();
    private Type firstStep;

    public Recipe(Type... steps)
    throws InvalidParameterException
    {
        if (steps.length == 0)
        {
            throw new InvalidParameterException("No types were passed to receip!");
        }
        
        firstStep = steps[0];
        for (int i = 1; i < steps.length; i++)
        {
            Type sourceType = steps[i - 1];
            Type destType = steps[i];

            if (Steps.get(sourceType) != null)
            {
                throw new InvalidParameterException("Invalid Type order given: repeating items not allowed.");
            }

            if (Steps.get(destType) != null)
            {
                throw new InvalidParameterException("Invalid Type order given: possible cycle.");
            }

            Steps.put(sourceType, destType);
        }
    }

    public Task First(int priority)
    {
        int id = Task.reserveIdRange(StepsCount());
        return priority != 0 ? 
            new PriorityTask(firstStep, priority, id) : 
            new Task(firstStep, id);
    }

    /**
     * Takes a task and returns the next one to be done
     */
    public Task Next(Task task, int priority)
    {
        if (task == null)
        {
            return First(priority);
        }

        Type nextType = Steps.get(task.Type);
        if (nextType == null)
        {
            return null;
        }

        if (priority != 0)
        {
            return new PriorityTask(nextType, task, priority);
        }
        return new Task(nextType, task);
    }

    public int StepsCount() { return 1 + Steps.size(); }

    public void SendToScheduler(Scheduler sched, int priority)
    {
        try {
            for (
                Task task = First(priority); 
                task != null; 
                task = Next(task, priority)
            ) {
                if (Debug)
                {
                    System.out.println(task);
                }
                
                synchronized (task) {
                    sched.Schedule(task);
                    task.wait(); // Release the lock, will be waked by task.notify() inside Station class
                }
            }
        } catch (Throwable ex) {
            System.err.println("Exception happended!");
            System.err.println(ex.getMessage());
            var trace = ex.getStackTrace();
            for (int i = 0; i < trace.length; i++)
            {
                System.err.println("\t#" + i + ": " + trace[i]);
            }
        }
    }

    public void SendToScheduler(Scheduler sched)
    {
        SendToScheduler(sched, 0);
    }
}
