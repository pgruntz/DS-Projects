package ch.ethz.inf.vs.a3.clock;

/**
 * Created by Patrick on 10/25/2016.
 */

public class LamportClock implements Clock {

    private int time;

    void setTime(int time){
        this.time=time;
    }

    int getTime() {
        return time;
    }

    @Override
    public void update(Clock other) {

    }

    @Override
    public void setClock(Clock other) {

    }

    @Override
    public void tick(Integer pid) {
        time++;
    }

    @Override
    public boolean happenedBefore(Clock other) {
        return false;
    }

    @Override
    public void setClockFromString(String clock) {

    }
}
