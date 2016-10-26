package ch.ethz.inf.vs.a3.clock;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;

/**
 * Created by Patrick on 10/25/2016.
 */

public class VectorClock implements Clock{

    private Map<Integer, Integer> vector = new HashMap<>();

    private Map<Integer, Integer> getVector(){
        return vector;
    }

    int getTime(Integer pid){
        return vector.get(pid);
    }

    void addProcess(Integer pid, int time){
        vector.put(pid,time);
    }

    @Override
    public void update(Clock other) {
        Map<Integer, Integer> temp = new HashMap<>();
        temp.putAll(((VectorClock)other).getVector());
        temp.putAll(vector);
        vector=temp;
        for (int pid:((VectorClock)other).getVector().keySet()){
            if(vector.get(pid) < ((VectorClock)other).getTime(pid))
                vector.put(pid,((VectorClock)other).getTime(pid));
        }
    }

    @Override
    public void setClock(Clock other) {
        vector = new HashMap<>();
        vector.putAll(((VectorClock)other).getVector());
    }

    @Override
    public void tick(Integer pid) {
        vector.put(pid,(vector.get(pid)+1));
    }

    @Override
    public boolean happenedBefore(Clock other) {
        for (int pid:vector.keySet()){
            if(vector.get(pid)>((VectorClock)other).getTime(pid))
                return false;
        }
        return true;
    }

    @Override
    public void setClockFromString(String clock) {
        Map<Integer, Integer> temp = new HashMap<>();
        int key,value;
        int i = clock.indexOf(":");
        int start = clock.indexOf("{")+2;

        while (i != -1) {
            int end = clock.indexOf(",", i);
            if(end == -1) end=clock.indexOf("}",i);
            String keyS = clock.substring(start,i-1);
            String valueS = clock.substring(i + 1, end);
            try{
                key = valueOf(keyS);
                value = valueOf(valueS);
                temp.put(key,value);
            } catch (NumberFormatException e) {
                return;
            }
            start = clock.indexOf("\"",i+1)+1;
            i = clock.indexOf(":", i + 1);
        }
        vector = new HashMap<>();
        vector.putAll(temp);
    }

    @Override
    public String toString() {
        if (vector.toString().equals("{}")) return "{}";
        return vector.toString().replaceAll("=","\":").replaceAll(", ",",\"").replaceAll("\\{","{\"");
    }
}
