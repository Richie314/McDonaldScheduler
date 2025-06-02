package simulation.simple;

public class Product1 {
    public int x;
    public Product1() throws InterruptedException
    {
        this((int) (Math.random() * 100000));
    }
    public Product1(int x) throws InterruptedException
    {
        System.out.println("\tStarting creation of Product1");
        for (int i = 0; i < 5; i++)
        {
            Thread.sleep(1000);
            System.out.println("\tProduct1: " + (i+1)*20 + "%");
        }
        this.x = x;
        System.out.println("\tCreated Product1 with x = " + x);
    }
}
