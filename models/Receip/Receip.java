package models.receip;
import java.lang.reflect.Type;

import javax.naming.OperationNotSupportedException;

import models.task.Task;

public abstract class Receip
{
    protected Type[] Stages = new Type[0]; // Fallback to empty implementations of LoadStages();

    private void LoadStagesIfNecessary() 
    throws OperationNotSupportedException
    {
        if (Stages.length == 0)
        {
            LoadStages();
        }
        if (Stages.length == 0)
        {
            throw new OperationNotSupportedException("No stages found. At least one is required");
        }
    }

    public Task First(int receipLength) throws Throwable
    {
        return Next(null, receipLength);
    }

    /**
     * Takes a task and returns the next one to be done
     */
    public Task Next(Task task, int receipLength) throws Throwable
    {
        LoadStagesIfNecessary();

        if (task == null)
        {
            int id = Task.reserveIdRange(receipLength);
            Task outTask = new Task(Stages[0]);
            outTask.setId(id);
            return outTask;
        }

        boolean takeNext = false;
        for (Type stage : Stages)
        {
            if (takeNext)
            {
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

    protected abstract void LoadStages();

    public synchronized int StepsCount() { return Stages.length; }
}
