package models.Receip;
import java.lang.reflect.Type;

public class SingleStepReceip
extends Receip
{
    private Type onlyType;
    public SingleStepReceip(Type type)
    {
        onlyType = type;
    }
    public void LoadStages()
    {
        Stages = new Type[] { onlyType };
    }
}
