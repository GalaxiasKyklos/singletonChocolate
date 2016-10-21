package mx.iteso;

import mx.iteso.singleton.ChocolateBoilerOnDemand;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Saúl on 21/10/2016.
 */
public class ChocolateBoilerOnDemandTest {
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

    @Test
    public void fillTest() {
        ChocolateBoilerOnDemand chocolateBoiler = ChocolateBoilerOnDemand.getInstance();
        chocolateBoiler.fill();
        Assert.assertEquals(chocolateBoiler.isEmpty(), false);
    }

    @Test
    public void drainTest() {
        ChocolateBoilerOnDemand chocolateBoiler = ChocolateBoilerOnDemand.getInstance();
        chocolateBoiler.drain();
        chocolateBoiler.boil();
        Assert.assertEquals(chocolateBoiler.isEmpty(), true);
    }

    @Test
    public void boilTest() {
        ChocolateBoilerOnDemand chocolateBoiler = ChocolateBoilerOnDemand.getInstance();
        chocolateBoiler.fill();
        chocolateBoiler.boil();
        Assert.assertEquals(chocolateBoiler.isBoiled(), true);
    }

    class ChocolateThread implements Runnable {
        CountDownLatch startSignal;
        CountDownLatch doneSignal;
        ChocolateBoilerOnDemand chocolateBoiler;

        public ChocolateThread(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.chocolateBoiler = null;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public ChocolateBoilerOnDemand getChocolateBoiler() {
            return chocolateBoiler;
        }

        public void run() {
            try {
                startSignal.await();
                this.chocolateBoiler = ChocolateBoilerOnDemand.getInstance();
                doneSignal.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
