package models.Receip;
import java.lang.reflect.Type;

public class SingleStepReceip
extends Receip
{
    public SingleStepReceip(Type type)
    {
        Stages = new Type[] { type };
    }
}
