package ch.ethz.inf.vs.a3.clock;

import java.util.Comparator;

import ch.ethz.inf.vs.a3.message.Message;

/**
 * Created by Patrick on 11/3/2016.
 */

public class VectorClockComparator implements Comparator<VectorClock> {

    @Override
    public int compare(VectorClock o1, VectorClock o2) {
            if(o1.happenedBefore(o2)) {
                return -1;
            } else if(o2.happenedBefore(o1)){
                return 1;
            } else {
                return 0;
            }

    }
}
