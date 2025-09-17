package models.Task;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

/**
 * A generic operation to do, 
 * takes an Input and after doing work delivers a Result of a specified type.
 */
public class Task
extends IncrementalIdentifier
{
    public Object Input = null;
    public Object Result = null;
    public Type Type;
    private Class<?> ResultClass;

    public int ReceipDepht = 0;

    public Task(Type type, int id)
    {
        super(id);

        this.Type = type;
        this.ResultClass = (Class<?>)type;
    }
    public Task(Type type, Task completedTask)
    {
        this(type, completedTask.getId() + 1);

        this.Input = completedTask.Result;
        this.ReceipDepht = completedTask.ReceipDepht + 1;
    }

    /**
     * Generates the result from the input
     */
    public synchronized void DoWork() throws Throwable
    {
        // The actual work happens when we call newInstance();

        Class<?>[] constructor_parameters_types = 
            (Input == null) ? 
                (new Class<?>[0]) : 
                new Class<?>[] { Input.getClass() };

        Object[] constructor_parameters =
            (Input == null) ? 
                (new Object[0]) : 
                new Object[] { Input };

        Constructor<?> builder = ResultClass.getConstructor(constructor_parameters_types);

        Result = builder.newInstance(constructor_parameters);
        notifyAll();
    }

    protected String getPadding()
    {
        String p = "";
        for (int i = 0; i < this.ReceipDepht; i++)
        {
            p += "  ";
        }
        return p;
    }

    @Override
    public synchronized String toString()
    {
        return
            getPadding() +
            "#" +
            this.getId() + 
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
