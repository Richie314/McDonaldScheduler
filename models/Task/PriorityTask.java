package models.task;

import java.lang.reflect.Type;

public class PriorityTask 
extends Task
{
    public int Priority = 0;
    public PriorityTask(Type type)
    {
        super(type);
    }
    public PriorityTask(Type type, Task completedTask)
    {
        super(type, completedTask);
    }
    public PriorityTask(Type type, int priority)
    {
        super(type);
        this.Priority = priority;
    }
    public PriorityTask(Type type, Task completedTask, int priority)
    {
        super(type, completedTask);
        this.Priority = priority;
    }

    @Override
    public synchronized String toString()
    {
        return
            getPadding() +
            "P" + this.Priority + 
            "#" + this.getId() + 
            "{" + 
            (Input != null ? Input.getClass().getName() : "nothing") + 
            " >>> " + 
            Type.getTypeName() + 
            " " + 
            (Result != null ? "(Completed)" : "(Running)") + 
            "}"
        ;
    }
}
