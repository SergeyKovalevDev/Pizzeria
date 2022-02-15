import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Pizzeria {
    private final BlockingDeque<Wagon> queue;
    private final long startTime;

    public Pizzeria() {
        queue = new LinkedBlockingDeque<>(2);
        queue.offer(new Wagon());
        queue.offer(new Wagon());
        startTime = System.currentTimeMillis();
    }

    public void order(String pizzaName) {
        if ((System.currentTimeMillis() - startTime) < 5000) {
            new Thread(new WagonStarter(pizzaName)).start();
        }
    }

    private void backInQueue(Wagon wagon) {
        queue.offerLast(wagon);
        System.out.println(wagon.getPizzaName() + " is delivered");
    }

    private class WagonStarter extends Thread {
        private final String pizzaName;

        public WagonStarter(String pizzaName) {
            this.pizzaName = pizzaName;
        }

        @Override
        public void run() {
            try {
                Wagon w = queue.pollFirst(250, TimeUnit.MILLISECONDS);
                if (w != null) {
                    w.setPizzaName(pizzaName);
                    new Thread(w).start();
                } else {
                    System.out.println(pizzaName + " is NOT delivered");
                }
            } catch (InterruptedException e) { // if interrupted while waiting
                e.printStackTrace();
            }
        }
    }

    private class Wagon extends Thread {
        private String pizzaName;

        public String getPizzaName() {
            return pizzaName;
        }

        public void setPizzaName(String pizzaName) {
            this.pizzaName = pizzaName;
        }

        @Override
        public void run() {
            try {
                sleep(500);
                backInQueue(this);
            } catch (InterruptedException e) { // if any thread has interrupted the current thread
                e.printStackTrace();
            }
        }
    }
}
