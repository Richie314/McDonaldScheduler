package simulation.multiple_receips;

public class Product5
{
    public Product5() throws InterruptedException
    {
        Thread.sleep(5000);
    }
    public Product5(Product4 p4) throws InterruptedException
    {
        this();
    }
}
