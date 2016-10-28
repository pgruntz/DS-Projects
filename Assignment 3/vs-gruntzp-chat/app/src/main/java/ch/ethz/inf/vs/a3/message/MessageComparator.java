package ch.ethz.inf.vs.a3.message;

//TODO: uncomment this
import java.util.Comparator;
import ch.ethz.inf.vs.a3.message.Message;

/**
 * Message comparator class. Use with PriorityQueue.*/
public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message lhs, Message rhs) {
        if(lhs.timestamp.happenedBefore(rhs.timestamp)) {
            return -1;
        } else if(rhs.timestamp.happenedBefore(lhs.timestamp)){
            return 1;
        } else {
            return 0;
        }
    }

}
