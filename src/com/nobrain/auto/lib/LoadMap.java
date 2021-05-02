package com.nobrain.auto.lib;

import com.nobrain.auto.clasz.PressInfo;
import com.nobrain.auto.clasz.Key;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadMap {
    public ArrayList<PressInfo> delays = new ArrayList<>();

    public LoadMap(String path) throws ParseException {

         JSONParser parser = new JSONParser();
         JSONObject map = (JSONObject) parser.parse(read(path).replaceAll("\uFEFF", ""));
         JSONObject setting = (JSONObject) map.get("settings");
         JSONArray actions = (JSONArray) map.get("actions");
         String[] pathData = map.get("pathData").toString().split("");
         HashMap<Integer, Double> changeBPM = new HashMap<>();
         HashMap<Integer, Boolean> changeTwirl = new HashMap<>();

         double currentBPM = toDouble(setting.get("bpm"));
         boolean isTwirl = false;

         String[] key2 ="jf".split("");
         String[] key4 ="jkfd".split("");
         String[] key6 ="jkfdsl".split("");
         String[] key8 ="jkfdsla;".split("");

         double pitch = toDouble(setting.get("pitch"))/100;
         double tick = toDouble(setting.get("countdownTicks"));
         double loadDelay = ((0.5 + ((0.45*tick)/((currentBPM*pitch)/100)))*1000);
         int offset = toInt(setting.get("offset"));
         double result = ((60000/(currentBPM*pitch)+offset+loadDelay)*1000000);

         delays.add(new PressInfo((long)result));

         for(Object o : actions) {
             JSONObject action = (JSONObject)o;
             if(action.get("eventType").equals("SetSpeed")) {
                 if(action.get("speedType")==null) {
                     currentBPM = toDouble(action.get("beatsPerMinute"));
                     changeBPM.put(toInt(action.get("floor"))-1, currentBPM);
                     continue;
                 }
                 if(action.get("speedType").equals("Bpm")) {
                     currentBPM = toDouble(action.get("beatsPerMinute"));
                     changeBPM.put(toInt(action.get("floor"))-1, currentBPM);
                 } else {
                     changeBPM.put(toInt(action.get("floor"))-1, currentBPM*toDouble(action.get("bpmMultiplier")));
                     currentBPM = currentBPM*toDouble(action.get("bpmMultiplier"));
                 }
             }
             if(action.get("eventType").equals("Twirl")) {
                 changeTwirl.put(toInt(action.get("floor"))-1,true);
             }
         }

         currentBPM = toDouble(setting.get("bpm"))*pitch;
         int i = 0;
         for (int n = 0; n < pathData.length; n++) {
             String now = pathData[n];
             boolean isMidspin = now.equals("!");

             if(isMidspin) n++;
             String next = getValue(pathData,n+1);

             if(changeBPM.get(n)!=null) currentBPM = changeBPM.get(n);
             if(changeTwirl.get(n)!=null) isTwirl = !isTwirl;

             int angle = AngleUtill.getCurrentAngle(now,next,isTwirl,isMidspin);
             double tempBPM = ((double)angle/180)*(60/(currentBPM*pitch));

             PressInfo pressInfo = new PressInfo((long)(tempBPM*1000000000));

             switch(Key.getKey((int)(tempBPM*1000))) {
                 case Key.KEY8:
                     if(i>=key8.length) i=0;
                     pressInfo.key = convert(key8[i]);
                     break;

                 case Key.KEY6:
                     if(i>=key6.length) i=0;
                     pressInfo.key = convert(key6[i]);
                     break;

                 case Key.KEY4:
                     if(i>=key4.length) i=0;
                     pressInfo.key = convert(key4[i]);
                     break;

                 case Key.KEY2:
                     if(i>=key2.length) i=0;
                     pressInfo.key = convert(key2[i]);
                     break;

                 case Key.KEY1:
                     if(i!=0) i=0;
                     pressInfo.key = convert(key2[i]);
                     break;
             }

             i++;
             delays.add(pressInfo);

         }
    }

    private String getValue(String[] array, int index) {
        if(index>=array.length-1) return array[array.length-1];
        return array[index];
    }

    private int toInt(Object o) {
        try {
            return Integer.parseInt(String.valueOf(o));
        } catch (Exception e) {
            return -1;
        }
    }


    private double toDouble(Object o) {
        try {
            return Double.parseDouble(String.valueOf(o));
        } catch (Exception e) {
            return -1;
        }
    }

    private int convert(String key) {
        return Integer.parseInt(key
                .replaceAll("j","74")
                .replaceAll("a","65")
                .replaceAll("s","83")
                .replaceAll("d","68")
                .replaceAll("k","75")
                .replaceAll("l","76")
                .replaceAll(";","59")
                .replaceAll("f","70"));
    }

    private String read(String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8));
            String result = "";
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains("floor")&&!line.contains("SetSpeed")&&!line.contains("Twirl")) continue;

                result += line + "\n";
            }
            return result;
        } catch (IOException e) {
            return "";
        }
    }
}
