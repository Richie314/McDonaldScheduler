package models.Receip;

import java.lang.reflect.Type;

public class TwoStepReceip
extends Receip
{
    private Type middleType, productType;
    public TwoStepReceip(Type middleType, Type productType)
    {
        this.middleType = middleType;
        this.productType = productType;
    }
    public void LoadStages()
    {
        Stages = new Type[] { middleType, productType };
    }
}
