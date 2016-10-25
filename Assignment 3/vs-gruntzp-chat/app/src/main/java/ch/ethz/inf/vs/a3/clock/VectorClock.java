package ch.ethz.inf.vs.a3.clock;

import java.util.Map;

/**
 * Created by Patrick on 10/25/2016.
 */

public class VectorClock implements Clock{

    private Map<Integer, Integer> vector;

    int getTime(Integer pid){
        return -1;
    }

    void addProcess(Integer pid, int time){
    }


    @Override
    public void update(Clock other) {

    }

    @Override
    public void setClock(Clock other) {

    }

    @Override
    public void tick(Integer pid) {

    }

    @Override
    public boolean happenedBefore(Clock other) {
        return false;
    }

    @Override
    public void setClockFromString(String clock) {

    }
}
