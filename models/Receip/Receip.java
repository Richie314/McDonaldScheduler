package models.Receip;
import java.lang.reflect.Type;

import javax.naming.OperationNotSupportedException;

import models.Task.PriorityTask;
import models.Task.Task;
import models.Scheduler.Scheduler;

public abstract class Receip
{
    public static boolean Debug = true;
    protected Type[] Stages = new Type[0];

    public Task First(int priority)
    throws OperationNotSupportedException
    {
        if (Stages.length == 0)
        {
            throw new OperationNotSupportedException("No stages found. At least one is required");
        }

        int id = Task.reserveIdRange(StepsCount());
        return priority != 0 ? 
            new PriorityTask(Stages[0], priority, id) : 
            new Task(Stages[0], id);
    }

    /**
     * Takes a task and returns the next one to be done
     */
    public Task Next(Task task, int priority)
    throws OperationNotSupportedException
    {
        if (task == null)
        {
            return First(priority);
        }
        
        if (Stages.length == 0)
        {
            throw new OperationNotSupportedException("No stages found. At least one is required");
        }

        boolean takeNext = false;
        for (Type stage : Stages)
        {
            if (takeNext)
            {
                if (priority != 0)
                {
                    return new PriorityTask(stage, task, priority);
                }
                return new Task(stage, task);
            }

            if (stage == task.Type)
            {
                // The task was producing the current product
                // We now have to make the next one using this as input parameter
                takeNext = true;
            }
        }
        return null;
    }

    public int StepsCount() { return Stages.length; }

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
