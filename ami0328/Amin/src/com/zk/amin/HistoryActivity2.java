package com.zk.amin;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import com.zk.bean.AirLen;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class HistoryActivity2 extends FragmentActivity{
	
	private static List<AirLen> data1 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.history_1);
		
		data1 = MainActivity.queryAllAL();
		//List<Meter> list =MainActivity.queryAll();
		Log.i("数据库2size:", data1.size()+"");
		if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
		
		
	}
	
	
	/**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public final static String[] months = new String[]{"气体流量", "送丝速度"};
        
        public final static String[] days = new String[]{};
        public final static List<String> times = new ArrayList<String>();
        public final static List<Integer> air = new ArrayList<Integer>();
        public final static List<Float> speed = new ArrayList<Float>();
        @SuppressWarnings("rawtypes")
		public static List<List> total_data = new ArrayList<List>();
        private LineChartView chartTop;
        private ColumnChartView chartBottom;

        private LineChartData lineData;
        private ColumnChartData columnData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_line_column_dependency, container, false);

         //   int j = 0;
            Log.i("data1size", data1.size()+"");
            int size = data1.size();
            for (int i = 36000; i >= 0; i-=10) {
            	
            	times.add(data1.get(size-1-i).getTime().substring(5, data1.get(size-1-i).getTime().length()));
				air.add(data1.get(size-1-i).getAir());
				speed.add(data1.get(size-1-i).getSi_speed());
			}
            total_data.add(air);
            total_data.add(speed);
            // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);

            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            generateColumnData();

            return rootView;
        }
        
        
        private void generateColumnData() {

            int numSubcolumns = 1;
            int numColumns = months.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(1f, ChartUtils.pickColor()));
                }

                axisValues.add(new AxisValue(i).setLabel(months[i]));

               columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextSize(20).setTextColor(Color.BLACK));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2).setTextColor(Color.WHITE).setTextSize(15));

            chartBottom.setColumnChartData(columnData);
            

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomEnabled(false);

            // chartBottom.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // SelectedValue sv = chartBottom.getSelectedValue();
            // if (!sv.isSet()) {
            // generateInitialLineData();
            // }
            //
            // }
            // });

        }
        
        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */
        private void generateInitialLineData() {
            int numValues = 360 ;
            //a电压
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i <= numValues; i++) {
                values.add(new PointValue(i, air.get(i)));
                axisValues.add(new AxisValue(i).setLabel(times.get(i)));
            }
            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);
            
            //b电压
            
            /*//a电压
            List<PointValue> values2 = new ArrayList<PointValue>();
            for (int i = 0; i <= numValues; i++) {
                values2.add(new PointValue(i, vol_A.get(i)));
            }
            Line line2 = new Line(values2);
            line.setColor(ChartUtils.COLOR_ORANGE).setCubic(true);*/
            
            List<Line> lines = new ArrayList<Line>();
            lines.add(line);
            //lines.add(line2);
            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(7).setName("时间"));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3).setTextColor(Color.BLACK).setAutoGenerated(true));

            
             
            chartTop.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);    
            chartTop.setLineChartData(lineData);

            
            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);
            

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 380, 10, 0);
            chartTop.setCurrentViewport(v);

        }
        
        private void generateLineData(int color, float range,int index) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            int i = 0;
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(),  (Float) total_data.get(index).get(i));
                i++;
            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }
        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                generateLineData(value.getColor(), 100,columnIndex);
            }

            @Override
            public void onValueDeselected() {

                generateLineData(ChartUtils.COLOR_GREEN, 0,0);

            }
        }

    }
}
