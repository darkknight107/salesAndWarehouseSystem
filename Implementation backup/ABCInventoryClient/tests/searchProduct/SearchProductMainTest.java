package searchProduct;

import homePage.SearchProductMain;
import javafx.application.Application;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class SearchProductMainTest {

    // Wrapper thread updates this if
    // the ABCInventoryClient application runs without a problem.
    // Declared volatile to ensure that writes are visible to every thread.
    private volatile boolean success = false;

    @Test
    public void testMain() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try {
                    Application.launch(SearchProductMain.class); // Run ABCInventoryClient application.
                    success = true;
                } catch(Throwable t) {
                    if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        // We expect to get this exception since we interrupted
                        // the ABCInventoryClient application.
                        success = true;
                        return;
                    }
                    // This is not the exception we are looking for so log it.
                    Logger.getLogger(SearchProductMain.class.getName()).log(Level.SEVERE, null, t);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting ABCInventoryClient application
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        assertTrue(success);
    }
}