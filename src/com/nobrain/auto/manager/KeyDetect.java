package com.nobrain.auto.manager;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;


public class KeyDetect implements NativeKeyListener {
    public static boolean canCancel = false;
    public boolean isFirst = true;

    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());

        if(key.equalsIgnoreCase("Insert")) {
            if(Controller.adofai==null) return;
            try {
                if(canCancel) {
                    Controller.adofai.cancel();
                    canCancel = false;
                } else {
                    Controller.adofai.start(isFirst);
                    if(isFirst) isFirst = false;
                    canCancel = true;
                }
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }

    }
    public void nativeKeyReleased(NativeKeyEvent e) {
    }
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    }



