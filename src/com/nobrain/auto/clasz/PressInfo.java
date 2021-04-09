package com.nobrain.auto.clasz;

public class PressInfo {
    public int key = 74;
    public long delay = 0;

    public PressInfo(long delay, int key){
        this.delay = delay;
        this.key = key;
    }

    public PressInfo(long delay){
        this.delay = delay;
    }

    public PressInfo(){

    }
}
