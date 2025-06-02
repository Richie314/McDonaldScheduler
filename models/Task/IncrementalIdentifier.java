package models.task;

public class IncrementalIdentifier
{
    private int id;
    private static int counter = 0;

    private static synchronized int generateId()
    {
        return ++counter;
    }

    public IncrementalIdentifier()
    {
        id = generateId();
    }

    public int getId()
    {
        return this.id;
    }
}
