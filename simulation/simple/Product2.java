package simulation.simple;

public class Product2 {
    public String x;
    public Product2(String x) throws InterruptedException
    {
        System.out.println("Starting creation of Product2");
        wait(1000);
        this.x = x;
        System.out.println("Created Product2 with x = \"" + x + "\"");
    }
    public Product2(Product1 p) throws InterruptedException
    {
        this(String.valueOf(p.x));
    }
}
