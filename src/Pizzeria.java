import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Pizzeria {
    private final BlockingDeque<PizzaCar> queue;
    private final long startTime;

    public Pizzeria() {
        queue = new LinkedBlockingDeque<>(2);
        queue.offer(new PizzaCar());
        queue.offer(new PizzaCar());
        startTime = System.currentTimeMillis();
    }

    public void order(String pizzaName) {
        if ((System.currentTimeMillis() - startTime) < 5000) {
            new Thread(() -> {
                try {
                    PizzaCar pizzaCar = queue.pollFirst(250, TimeUnit.MILLISECONDS);
                    if (pizzaCar != null) {
                        pizzaCar.setPizzaName(pizzaName);
                        Thread t = new Thread(pizzaCar);
                        t.start();
                        t.join();
                        System.out.println(pizzaName + " is delivered");
                        queue.offerLast(pizzaCar);
                    } else {
                        System.out.println(pizzaName + " is NOT delivered");
                    }
                } catch (InterruptedException e) { // if interrupted while waiting
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private class PizzaCar implements Runnable {
        private String pizzaName;

        public void setPizzaName(String pizzaName) {
            this.pizzaName = pizzaName;
        }

        @Override
        public void run() {
            try {
                System.out.println("Begin to deliver " + pizzaName);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
