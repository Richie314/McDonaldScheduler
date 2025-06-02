package models.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * A generic operation to do, 
 * takes an Input and after doing work delivers a Result of a specified type.
 */
public class Task
{
    public Object Input = null;
    public Object Result = null;
    public Type Type;
    private Class<?> ResultClass;

    public Task(Type type)
    {
        this.Type = type;
        this.ResultClass = (Class<?>)type;
    }
    public Task(Type type, Object input)
    {
        this(type);
        this.Input = input;
    }

    /**
     * Generates the result from the input
     */
    public synchronized void DoWork() throws Throwable
    {
        // System.out.println("\tDoing task work...");
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
        // System.out.println("\tUsing builder " + builder.toString());

        Result = builder.newInstance(constructor_parameters);
        notifyAll();
    }

    public synchronized String GetSignature()
    {
        return 
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
