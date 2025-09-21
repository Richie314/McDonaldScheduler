package models.Receip;

import java.lang.reflect.Type;

public class TwoStepReceip
extends Receip
{
    public TwoStepReceip(Type middleType, Type productType)
    {
        Stages = new Type[] { middleType, productType };
    }
}
