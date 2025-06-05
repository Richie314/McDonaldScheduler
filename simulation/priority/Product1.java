package simulation.priority;

public class Product1 {
    public int x;
    public Product1() throws InterruptedException
    {
        this((int) (Math.random() * 100000));
    }
    public Product1(int x) throws InterruptedException
    {
        Thread.sleep(1000);
        this.x = x;
    }
}
