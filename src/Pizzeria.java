import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Pizzeria {
    private final BlockingDeque<Object> queue;
    private final long startTime;

    public Pizzeria() {
        queue = new LinkedBlockingDeque<>(2);
        queue.offer(new Object());
        queue.offer(new Object());
        startTime = System.currentTimeMillis();
    }

    public void order(String pizzaName) {
        if ((System.currentTimeMillis() - startTime) < 5000) {
            new Thread(() -> {
                try {
                    Object o = queue.pollFirst(250, TimeUnit.MILLISECONDS);
                    if (o != null) {
                        Thread.sleep(500);
                        System.out.println(pizzaName + " is delivered");
                        queue.offerLast(o);
                    } else {
                        System.out.println(pizzaName + " is NOT delivered");
                    }
                } catch (InterruptedException e) { // if interrupted while waiting
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
