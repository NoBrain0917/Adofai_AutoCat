package com.nobrain.auto.lib;

import com.nobrain.auto.clasz.Key;
import com.nobrain.auto.clasz.PressInfo;
import com.nobrain.auto.manager.KeyDetect;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Adofai {
    public boolean isCancel = false;
    public static Thread thread;
    private List<PressInfo> delayList;
    private CheckBox workshop;
    private TextField lag;
    private TextField name;
    private Label isOn;
    private long original = 0 ;


    public Adofai(String path,TextField lag,TextField name, Label isOn,CheckBox workshop) throws ParseException {
        isCancel = false;
        this.lag = lag;
        this.name = name;
        this.workshop = workshop;
        this.isOn = isOn;

        if (!workshop.isSelected() && path != null) {
            delayList = new LoadMap(path).delays;
        } else {
            SearchMap.DistanceClass map = new SearchMap(name.getText()).result;
            delayList = new LoadMap(map.path.getAbsolutePath()).delays;
            name.setText(map.title);
        }
    }

    public void cancel(){
        isCancel = true;
        isOn.setVisible(false);

        if(thread!=null) if (!thread.isInterrupted()) thread.interrupt();
    }

    public void start(Boolean isFirst) throws AWTException {
        isCancel = false;
        isOn.setVisible(true);

        final Robot robot = new Robot();
        robot.keyPress('P');
        robot.keyRelease('P');

        if(original!=0) {
            PressInfo pressInfo = new PressInfo(original);
            delayList.set(0,pressInfo);
        }

        if(thread!=null) if(!thread.isInterrupted()) thread.interrupt();

        List<PressInfo> delays = delayList;
        long originalNum = delays.get(0).delay;
        if(original==0) original = originalNum;
        String text = lag.getText();
        double lagDelay = 0;

        try {
            double num = Double.parseDouble(text);
            if (!Double.isNaN(num)) {
                lagDelay = num;
            }
        } catch (Exception e) {
        }

        if(isFirst)
            lagDelay-=120;

        PressInfo pressInfo = new PressInfo(originalNum+(long)lagDelay*1000000);


        delays.set(0,pressInfo);
        thread = new Thread(() -> {
            Iterator<PressInfo> pressInterator = delays.iterator();

            long nowTime, prevTime = System.nanoTime();
            int prev = 0, now, delay = 55;

            PressInfo press = pressInterator.next();

            while (true) {
                if(isCancel) break;
                nowTime = System.nanoTime();

                if (nowTime - prevTime >= press.delay) {
                    prevTime += press.delay;
                    delay = 55;

                    if (pressInterator.hasNext()) {
                        press = pressInterator.next();
                    } else {
                        isOn.setVisible(false);
                        break;
                    }


                    PressInfo finalPress = press;
                    now = (int)(finalPress.delay/1000000);

                    robot.keyPress(finalPress.key);

                    if(now<55&&prev<55) {
                        delay = now-5;
                        if(delay<0) delay = 0;
                    }

                    try {
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() { robot.keyRelease(finalPress.key);
                                timer.cancel();
                                timer.purge();
                                }
                            };
                        timer.schedule(timerTask, delay);
                    } catch (Exception e) {
                    }


                    prev = (int)(finalPress.delay/1000000);
                }
            }
            thread.interrupt();
            KeyDetect.canCancel = false;
        });
        thread.start();
    }

}
