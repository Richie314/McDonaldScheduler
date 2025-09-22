package simulation.big;

public class ProductB2 {
    public String x;
    public ProductB2(String x) throws InterruptedException
    {
        Thread.sleep(1000);
        this.x = x;
    }
    public ProductB2(ProductB1 p) throws InterruptedException
    {
        this(String.valueOf(p.x));
    }
}
