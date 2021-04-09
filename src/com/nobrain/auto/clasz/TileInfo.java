package com.nobrain.auto.clasz;

public class TileInfo {
    public String angle;
    public boolean isMidspin = false;
    public int realPath = 0;

    public TileInfo(String angle, boolean isMidspin, int rp) {
        this.angle = angle;
        this.isMidspin = isMidspin;
        this.realPath = rp;
    }
}
