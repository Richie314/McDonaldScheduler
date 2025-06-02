package simulation.simple;

public class Product2 {
    public String x;
    public Product2(String x) throws InterruptedException
    {
        System.out.println("\tStarting creation of Product2");
        for (int i = 0; i < 5; i++)
        {
            Thread.sleep(1000);
            System.out.println("\tProduct2: " + (i+1)*20 + "%");
        }
        this.x = x;
        System.out.println("\tCreated Product2 with x = \"" + x + "\"");
    }
    public Product2(Product1 p) throws InterruptedException
    {
        this(String.valueOf(p.x));
    }
}
