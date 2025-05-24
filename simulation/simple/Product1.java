package simulation.simple;
import java.util.Optional;

public class Product1 {
    public int x;
    public Product1(Optional<Integer> x) throws InterruptedException
    {
        wait(1000);
        if (x.isPresent())
        {
            this.x = x.get();
        } else {
            this.x = (int) (Math.random() * 100000);
        }
    }
}
