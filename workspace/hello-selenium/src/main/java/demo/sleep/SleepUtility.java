package demo.sleep;

public class SleepUtility {

    private SleepUtility() {
        // Questo costruttore privato impedisce l'istanziazione della classe.
    }

    public static void sleepMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Gestisci l'eccezione o inoltrala se necessario.
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Ripristina l'interruzione del thread.
        }
    }
}

