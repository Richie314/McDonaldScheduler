package models.receip;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;

public class ArrayReceip
extends Receip
{
    public ArrayReceip(Type... stages)
    throws InvalidParameterException
    {
        if (stages.length == 0)
        {
            throw new InvalidParameterException("No types were passed to receip!");
        }
        this.Stages = stages;
    }
    public void LoadStages() { }
}
