package ch.ethz.inf.vs.a1.gruntzp.sensors;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Patrick on 10/4/2016.
 */
public class GraphContainerImpl implements GraphContainer {
    public GraphContainerImpl(GraphView graph){this.graph = graph;}
    private GraphView graph;


    @Override
    public void addValues(double xIndex, float[] values) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(xIndex, values[0]),
        });
        graph.addSeries(series);

    }

    @Override
    public float[][] getValues() {
        return new float[0][];
    }
}
