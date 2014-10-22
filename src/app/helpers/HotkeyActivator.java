package app.helpers;

import app.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.application.Platform;

/**
 * Created by jolly on 21/10/14.
 */
public class HotkeyActivator implements NativeKeyListener {

    private Main main;

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_W &&
            (e.getModifiers() == NativeInputEvent.ALT_L_MASK || e.getModifiers() == NativeInputEvent.ALT_R_MASK)) {
            Platform.runLater(() -> {
                main.getRootViewManager().setAndFocusInputField("activated");
            });
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public HotkeyActivator() {
        boolean errorShown = false;

        // Clear previous logging configurations.
        LogManager.getLogManager().reset();

        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            if (!errorShown) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
                errorShown = true;
            }
        }

        //Construct the example object and initialze native hook.
        GlobalScreen.getInstance().addNativeKeyListener(this);
    }

    public void setMainApp(Main main) {
        this.main = main;
    }
}
