package models.task;

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
        this.ResultClass = type.getClass();
    }
    public Task(Type type, Object input)
    {
        this(type);
        this.Input = input;
    }

    /**
     * Generates the result from the input
     */
    public void DoWork() throws Throwable
    {
        System.out.println("\tDoing task work...");
        // The actual work happens when we call newInstance();

        if (Input == null)
        {
            Result = ResultClass.getConstructor().newInstance();
        } else {
            Result = ResultClass
                .getConstructor(Input.getClass())
                .newInstance(Input); 
        }
    }

    public String GetSignature()
    {
        return (Result != null ? "Created " : "Creating ") + 
            Type.getTypeName() + 
            " from " +
            (Input != null ? Input.getClass().getName() : "nothing");
    }
}
