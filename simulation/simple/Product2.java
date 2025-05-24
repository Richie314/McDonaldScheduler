package simulation.simple;

public class Product2 {
    public String x;
    public Product2(String x) throws InterruptedException
    {
        wait(1000);
        this.x = x;
    }
    public Product2(Product1 p) throws InterruptedException
    {
        this(String.valueOf(p.x));
    }
}
