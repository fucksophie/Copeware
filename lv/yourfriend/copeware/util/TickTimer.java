package lv.yourfriend.copeware.util;

/*
* TickTimer - https://github.com/CCBlueX/LiquidBounce/blob/legacy/shared/main/java/net/ccbluex/liquidbounce/utils/timer/TickTimer.java
* 			  CCBlueX and Contributors
*/

public final class TickTimer {

    private int tick;

    public void update() {
        tick++;
    }

    public void reset() {
        tick = 0;
    }

    public boolean hasTimePassed(final double d) {
        return tick >= d;
    }
}