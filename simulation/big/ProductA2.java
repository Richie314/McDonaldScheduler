package simulation.big;

public class ProductA2 {
    public String x;
    public ProductA2(String x) throws InterruptedException
    {
        Thread.sleep(1000);
        this.x = x;
    }
    public ProductA2(ProductA1 p) throws InterruptedException
    {
        this(String.valueOf(p.x));
    }
}
