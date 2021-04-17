package com.nobrain.auto.lib;

import com.nobrain.auto.clasz.Angle;

public class AngleUtill {
    public static int toAngle(String code){
        if (code.equals("L")) return Angle.Left;
        if (code.equals("Q")) return Angle.LeftUp;
        if (code.equals("U")) return Angle.Up;
        if (code.equals("E")) return Angle.RightUp;
        if (code.equals("R")) return Angle.Right;
        if (code.equals("C")) return Angle.RightDown;
        if (code.equals("D")) return Angle.Down;
        if (code.equals("Z")) return Angle.LeftDown;
        if (code.equals("H")) return Angle.T1LeftUp;
        if (code.equals("G")) return Angle.T2LeftUp;
        if (code.equals("W")) return Angle.S1LeftUp;
        if (code.equals("q")) return Angle.S2LeftUp;
        if (code.equals("p")) return Angle.S1RightUp;
        if (code.equals("o")) return Angle.S2RightUp;
        if (code.equals("T")) return Angle.T1RightUp;
        if (code.equals("J")) return Angle.T2RightUp;
        if (code.equals("M")) return Angle.T1RightDown;
        if (code.equals("B")) return Angle.T2RightDown;
        if (code.equals("A")) return Angle.S1RightDown;
        if (code.equals("Y")) return Angle.S2RightDown;
        if (code.equals("F")) return Angle.T1LeftDown;
        if (code.equals("N")) return Angle.T2LeftDown;
        if (code.equals("x")) return Angle.S1LeftDown;
        if (code.equals("V")) return Angle.S2LeftDown;
        if (code.equals("S5")) return Angle.S5;
        if (code.equals("S6")) return Angle.S6;
        if (code.equals("S7")) return Angle.S7;
        if (code.equals("S8")) return Angle.S8;
        return 0;
    }

    public static int getCurrentAngle(String thisTile, String nextTile, Boolean isTwirl, Boolean isMidspin) {
        int angle = (toAngle(nextTile)-toAngle(thisTile));
        angle += (isMidspin)? 360:540;
        angle %= 360;
        if(isTwirl) angle = 360-angle;
        if(angle==0) angle = 360;
        return angle;
    }
}
