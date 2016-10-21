package mx.iteso;

import mx.iteso.singleton.ChocolateBoiler;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Saúl on 21/10/2016.
 */
public class ChocolateBoilerTest {
    @Test
    public synchronized void testSynch() throws Exception {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(2);

        Thread[] instances = new Thread[2];
        ChocolateThread[] instance = new ChocolateThread[2];
        for (int i = 0; i < 2; i++) {
            instance[i] = new ChocolateThread(startSignal, doneSignal);
        }

        for (int i = 0; i < 2; i++) {
            instances[i] = new Thread(instance[i]);
            instances[i].start();

        }
        startSignal.countDown();
        doneSignal.await();
        for (int i = 0; i < 2; i++) {
            for (int j = i; j < instances.length; j++) {
                if (instance[i].getChocolateBoiler() != instance[j].getChocolateBoiler())
                {
                    throw (new Exception());
                }
            }
        }
    }

    class ChocolateThread implements Runnable {
        CountDownLatch startSignal;
        CountDownLatch doneSignal;
        ChocolateBoiler chocolateBoiler;

        public ChocolateThread(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.chocolateBoiler = null;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public ChocolateBoiler getChocolateBoiler() {
            return chocolateBoiler;
        }

        public void run() {
            try {
                startSignal.await();
                this.chocolateBoiler = ChocolateBoiler.getInstance();
                doneSignal.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
