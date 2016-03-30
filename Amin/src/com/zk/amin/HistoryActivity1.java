package com.zk.amin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
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

import com.lidroid.xutils.exception.DbException;
import com.zk.bean.Meter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.TextView;

public class HistoryActivity1 extends FragmentActivity {
	
	private static List<Meter> data1 ;
	private static TextView tv_selectTime;
	static Calendar cal;
	private static Context mContext;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.history_1);
		mContext = this;
		
	
		try {
			data1 = MainActivity.getMostNewdata();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//List<Meter> list =MainActivity.queryAll();
//		Log.i("数据库size:", data1.size()+"");
		if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
		
		
	}
	
	
	/**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment  {
        public final static String[] months = new String[]{"A相电压", "B相电压", "C相电压", "A相电流", "B相电流", "C相电流","耗电量","直流电压","直流电流","当前气体","总气体","送丝速度"};
        
        public final static String[] days = new String[]{};
        public final static List<String> times = new ArrayList<String>();
        public final static List<Float> vol_A = new ArrayList<Float>();
        public final static List<Float> vol_B = new ArrayList<Float>();
        public final static List<Float> vol_C = new ArrayList<Float>();
        public final static List<Float> ele_A = new ArrayList<Float>();
        public final static List<Float> ele_B = new ArrayList<Float>();
        public final static List<Float> ele_C = new ArrayList<Float>();
        public final static List<Float> ene = new ArrayList<Float>();
        public final static List<Float> zhiliu_V = new ArrayList<Float>();
        public final static List<Float> zhiliu_A = new ArrayList<Float>();
        public final static List<Float> air_current = new ArrayList<Float>();
        public final static List<Float> air_total = new ArrayList<Float>();
        public final static List<Float> si_speed = new ArrayList<Float>();
        public static List<List<Float>> total_data = new ArrayList<List<Float>>();
        private LineChartView chartTop;
        private ColumnChartView chartBottom;

        private LineChartData lineData;
        private ColumnChartData columnData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_line_column_dependency, container, false);
        	
            int j = 0;
            Log.i("data1size", data1.size()+"");
            int size = data1.size();
            cal = Calendar.getInstance();
            
            for (int i = 0; i <= 3600; i+=10) {
            	
				times.add(data1.get(size-1-i).getTime().substring(5, data1.get(size-1-i).getTime().length()));
				vol_A.add(data1.get(size-1-i).getVa());
				vol_B.add(data1.get(size-1-i).getVb());
				vol_C.add(data1.get(size-1-i).getVc());
				ele_A.add(data1.get(size-1-i).getIa());
				ele_B.add(data1.get(size-1-i).getIb());
				ele_C.add(data1.get(size-1-i).getIc());
				ene.add(data1.get(size-1-i).getEne());
				zhiliu_V.add((float) ((data1.get(size-1-i).getZhiliu1()-39321)*45/26214));
				zhiliu_A.add((float) ((data1.get(size-1-i).getZhiliu2()-39321)*600/26214));
				air_current.add(data1.get(size-1-i).getAir_current());
				air_total.add(data1.get(size-1-i).getAir_total());
				si_speed.add(data1.get(size-1-i).getSi_speed());
			}
            total_data.add(vol_A);
            total_data.add(vol_B);
            total_data.add(vol_C);
            total_data.add(ele_A);
            total_data.add(ele_B);
            total_data.add(ele_C);
            total_data.add(ene);
            total_data.add(zhiliu_V);
            total_data.add(zhiliu_A);
            total_data.add(air_current);
            total_data.add(air_total);
            total_data.add(si_speed);
            // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);
//            tv_selectTime = (TextView) rootView.findViewById(R.id.select_time);
//    		tv_selectTime.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//					new DatePickerDialog(mContext, listener,
//							cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
//							cal.get(Calendar.DAY_OF_MONTH)).show();
//					
//				}
//			});
            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            generateColumnData();

          return rootView;
        }
        
        
        private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() { //
    		@Override
    		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

    			cal.set(Calendar.YEAR, arg1);
    			cal.set(Calendar.MONTH, arg2);
    			cal.set(Calendar.DAY_OF_MONTH, arg3);
    		}
    	};
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
            columnData.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.WHITE).setTextSize(15));

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
                values.add(new PointValue(i, vol_A.get(i)));
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
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(1).setName("时间"));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3).setTextColor(Color.BLACK).setAutoGenerated(true).setName("电流/电压/电能"));

            
//            chartTop.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL); 
            chartTop.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL); 
            chartTop.setLineChartData(lineData);
            chartTop.setZoomEnabled(true);

            
            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);
            chartTop.setInteractive(true);
            // And set initial max viewport and current viewport- remember to set viewports after data.

           final  Viewport v = new Viewport(chartTop.getMaximumViewport());
            v.bottom = 0;
           chartTop.setMaximumViewport(v);
           chartTop.setCurrentViewport(v);
           chartTop.setViewportCalculationEnabled(false);
        
        }
        
        private void generateLineData(int color, float range,int index) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

           
            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
           // lineData.setAxisYLeft(Axis.generateAxisFromCollection(total_data.get(index)));
            
            line.setColor(color);
            int i = 0;
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(),  total_data.get(index).get(i));
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


	
	

	/**
	 * 最小时间
	 */
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() { //
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

			cal.set(Calendar.YEAR, arg1);
			cal.set(Calendar.MONTH, arg2);
			cal.set(Calendar.DAY_OF_MONTH, arg3);
			updateDate();
		}
	};
	
	private void updateDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	//	List data = MainActivity.queryByTime(df.format(cal.getTime()));
		//Log.i("listaaa", data.size()+"");
	};

  }


	
