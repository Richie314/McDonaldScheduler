package models.receip;

import java.lang.reflect.Type;

public class ArrayReceip
extends Receip
{
    public ArrayReceip(Type[] stages)
    {
        this.Stages = stages;
    }
    public void LoadStages() { }
}
