package mx.iteso.singleton;

/**
 * Created by Saúl on 20/10/2016.
 */
public class ChocolateBoiler {
    private boolean empty;
    private boolean boiled;
    private static volatile ChocolateBoiler uniqueInstance;
    private static final Object lock = new Object();

    private ChocolateBoiler() {
        empty = true;
        boiled = false;
    }

    // Double check method
    public static ChocolateBoiler getInstance() {
        ChocolateBoiler instance = uniqueInstance;

        if (instance == null) {
            synchronized (lock) {
                instance = uniqueInstance;
                if (instance == null) {
                    instance = new ChocolateBoiler();
                    uniqueInstance = instance;
                }
            }
        }
        return instance;
    }

    public void fill() {
        if (isEmpty()) {
            empty = false;
            boiled = false;
            // fi ll the boiler with a milk/chocolate mixture
        }
    }
    public void drain() {
        if (!isEmpty() && isBoiled()) {
            // drain the boiled milk and chocolate
            empty = true;
        }
    }
    public void boil() {
        if (!isEmpty() && !isBoiled()) {
            // bring the contents to a boil
            boiled = true;
        }
    }
    public boolean isEmpty() {
        return empty;
    }
    public boolean isBoiled() {
        return boiled;
    }
}
