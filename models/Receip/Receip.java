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

    public Task First() throws Throwable
    {
        return Next(null);
    }

    /**
     * Takes a task and returns the next one to be done
     */
    public Task Next(Task task) throws Throwable
    {
        LoadStagesIfNecessary();

        if (task == null)
        {
            return new Task(Stages[0]);
        }

        boolean takeNext = false;
        for (Type stage : Stages)
        {
            if (takeNext)
            {
                return new Task(stage, task.Result, task.ReceipDepht + 1);
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
}
