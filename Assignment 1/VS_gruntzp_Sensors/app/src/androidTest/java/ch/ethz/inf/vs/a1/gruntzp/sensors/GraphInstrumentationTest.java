package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ethz.inf.vs.a1.gruntzp.sensors.GraphContainer;
import ch.ethz.inf.vs.a1.gruntzp.sensors.SensorActivity;

@RunWith(AndroidJUnit4.class)
public class GraphInstrumentationTest
        extends ActivityInstrumentationTestCase2<SensorActivity> {

    SensorActivity mActivity;
    private GraphContainer graphContainer;

    // For exception handling
    private boolean ret = false;

    public GraphInstrumentationTest() {
        super(SensorActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Intent sensorIntent = new Intent(getInstrumentation().getTargetContext(), SensorActivity.class);
        sensorIntent.putExtra("sensor_index", 0);
        setActivityIntent(sensorIntent);
        mActivity = getActivity();
        assertNotNull(mActivity);
        graphContainer = mActivity.getGraphContainer();
        assertNotNull(graphContainer);
    }

    /*
     * Simple test. 10 values.
     */
    @Test
    public void test1() {
        float[][] values = createArray(10, 3, 1);
        carryOutTest(values, values);
    }

    /*
     * Exactly 100 values
     */
   @Test
    public void test2() {
        float[][] values = createArray(100, 3, 1);
        carryOutTest(values, values);
    }

    /*
     * 101 values. Last 100 should be retrieved
     */
    @Test
    public void test3() {
        float[][] values = createArray(101, 3, 1);
        float[][] target = createArray(100, 3, 4);
        carryOutTest(values, target);
    }

    /*
     * 200 values. Last 100 should be retrieved.
     */
    @Test
    public void test4() {
        float[][] values = createArray(200, 3, 1);
        float[][] target = createArray(100, 3, 301);
        carryOutTest(values, target);
    }

    /*
     * 1000 values. Last 100 should be retrieved.
     */
    @Test
    public void test5() {
        float[][] values = createArray(1000, 3, 1);
        float[][] target = createArray(100, 3, 2701);
        carryOutTest(values, target);
    }

    /*
     * 0 values. Should retrieve an array with a length of 0.
     */
    @Test
    public void test6() {
        float[][] values = createArray(0, 3, 1);
        carryOutTest(values, values);
    }

    /*
     * Starting with value arrays of length 3, then switching to a length of 1. NullPointerException should not be thrown. 
     */
    @Test
    public void test7() {
        float[][] values = {{1.0f, 1.0f, 1.0f}, {1.0f}};
        carryOutTest(values, values);
    }

    /*
     * Starting with value arrays of length 1, then switching to a length of 3. There should be an exception. 
     */
    @Test
    public void test8() {
        float[][] values = {{1.0f}, {1.0f, 1.0f, 1.0f}};
        try {
            carryOutTest(values, values);
        } catch (NullPointerException n) {
            fail();
        } catch (Exception e) {
            // Pass
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        mActivity = null;
        graphContainer = null;
    }

    private void carryOutTest(final float[][] values, float[][] target) {
        ret = false;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < values.length; i++) {
                    try {
                        graphContainer.addValues(i + 1, values[i]);
                    } catch (NullPointerException n) {
                        fail();
                    } catch (Exception e) {
                        // Pass
                        ret = true;
                    }
                }
            }
        });
        getInstrumentation().waitForIdleSync();
        if(ret){
            return;
        }
        float[][] retrievedValues = graphContainer.getValues();
        assertEquals(target.length, retrievedValues.length);
        for (int i = 0; i < target.length; i++) {
            assertEquals(target[i].length, retrievedValues[i].length);
            for (int j = 0; j < target[0].length; j++) {
                assertEquals(target[i][j], retrievedValues[i][j]);
            }
        }
    }

    private float[][] createArray(final int dimM, final int dimN, final int startCounter) {
        int counter = startCounter;
        float[][] values = new float[dimM][];
        for (int i = 0; i < dimM; i++) {
            values[i] = new float[dimN];
            for (int j = 0; j < dimN; j++) {
                values[i][j] = (float) counter;
                counter++;
            }
        }
        return values;
    }
}