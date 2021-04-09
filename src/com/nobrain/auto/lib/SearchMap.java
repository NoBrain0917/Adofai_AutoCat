package com.nobrain.auto.lib;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SearchMap {
    public DistanceClass result;

    public static class DistanceClass {
        public double num = 0;
        public String title = "SANS";
        public File path;

        public DistanceClass(){
        }

        public DistanceClass(double num, String title, File path){
            this.num = num;
            this.title = title;
            this.path = path;
        }
    }

    public SearchMap(String name){
        File path = new File(getDrive());
        File[] files = path.listFiles();
        DistanceClass max = new DistanceClass();

        if(files==null) return;
        for (File file : files) {
            File main = new File(file.getAbsolutePath()+"/main.adofai");

            if(main.canRead()) {
                String title = read(main.getAbsolutePath()).split("\"song\": \"")[1].split("\", ")[0].replaceAll("<[^>]*>", " ").replaceAll(" {2}","").trim();
                double distance = similarity(name,title);
                if(max.num<distance) {
                    max = new DistanceClass(distance, title, main);
                }
            }
        }

        this.result = max;
    }

    private String getDrive(){
        File c = new File("C:/Program Files (x86)/Steam/steamapps/workshop/977950/");
        File d = new File("D:/SteamLibrary/steamapps/workshop/content/977950/");
        File e = new File("E:/SteamLibrary/steamapps/workshop/content/977950/");

        if(d.canRead()) return d.getAbsolutePath();
        if(e.canRead()) return e.getAbsolutePath();
        return c.getAbsolutePath();
    }

    private String read(String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8));
            String result = "";
            String line;
            while ((line = reader.readLine()) != null) {
                result += line+"\n";
                if(result.contains("author")) break;
            }
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    private double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) return 1.0;
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
