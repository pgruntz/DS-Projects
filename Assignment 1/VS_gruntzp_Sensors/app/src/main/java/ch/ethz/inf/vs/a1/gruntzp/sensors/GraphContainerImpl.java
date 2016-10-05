package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Iterator;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

/**
 * Created by Patrick on 10/4/2016.
 */
public class GraphContainerImpl implements GraphContainer {
    private GraphView graph;
    private LineGraphSeries<DataPoint> serie1 = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> serie2 = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> serie3 = new LineGraphSeries<>();


    public GraphContainerImpl(GraphView graph){
        this.graph = graph;
        graph.addSeries(this.serie1);
        graph.addSeries(this.serie2);
        graph.addSeries(this.serie3);

        serie1.setColor(BLUE);
        serie1.setDataPointsRadius(50);
        serie1.setBackgroundColor(BLUE);

        serie2.setColor(RED);
        serie2.setDataPointsRadius(50);
        serie2.setBackgroundColor(RED);

        serie1.setColor(GREEN);
        serie1.setDataPointsRadius(50);
        serie1.setBackgroundColor(GREEN);

        Viewport vp = graph.getViewport();
        vp.setScrollable(true);
        vp.setXAxisBoundsManual(true);
        //vp.setMinX(0);
        vp.setMaxX(8000);
    }




    @Override
    public void addValues(double xIndex, float[] values) {

        if (values.length == 1) {
            serie1.appendData(new DataPoint(xIndex, values[0]), true, 100);
        } else {
            serie1.appendData(new DataPoint(xIndex, values[0]), true, 100);
            serie2.appendData(new DataPoint(xIndex, values[1]), true, 100);
            serie3.appendData(new DataPoint(xIndex, values[2]), true, 100);
        }


        //graph.removeAllSeries();

        //graph.addSeries(serie1);
        //graph.addSeries(serie2);
        //graph.addSeries(serie3);
    }
    @Override
    public float[][] getValues() {
        //TODO
        Iterator<DataPoint> values = serie1.getValues(0, 1000);
        return new float[0][];
    }
}
