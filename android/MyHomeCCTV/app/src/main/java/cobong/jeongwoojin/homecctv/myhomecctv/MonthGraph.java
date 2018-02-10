package cobong.jeongwoojin.homecctv.myhomecctv;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthGraph.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BarChart barChart;
    private BarChart barChart2;
    private BarChart barChart3;

    YearXAxisFormatter yearXAxisFormatter;
    JSONArray jsonArray;

    String year;
    int list_cnt;

    float avgmonthtem[];
    float avgmonthhum[];
    float avgmonthppm[];

    ViewGroup viewGroup;

    GetMonthAvg getMonthAvg;


    private OnFragmentInteractionListener mListener;

    public MonthGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthGraph newInstance(String param1, String param2) {
        MonthGraph fragment = new MonthGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MonthGraph newInstance(String param1) {
        MonthGraph fragment = new MonthGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_month_graph,container,false);

        year = mParam1;

        //그래프 그리기 쓰레드 실행
        getMonthAvg = new GetMonthAvg();
        getMonthAvg.start();

        barChart = (BarChart)viewGroup.findViewById(R.id.barchart);
        barChart2 = (BarChart)viewGroup.findViewById(R.id.barchart2);
        barChart3 = (BarChart)viewGroup.findViewById(R.id.barchart3);

        return viewGroup;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //x축 변환 (월별)
    class YearXAxisFormatter implements IAxisValueFormatter
    {
        protected String[] mMonths = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        public YearXAxisFormatter() {
            // maybe do something here or provide parameters in constructor
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            float percent = value / axis.mAxisRange;
            return mMonths[(int) (mMonths.length * percent)];
        }
    }

    public class GetMonthAvg extends Thread{

        @Override
        public void run() {
            super.run();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://192.168.219.136/AvgMonth.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    //요청 성공시
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //그래프 그리기
                           drawGraph(response);

                       }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "[" + error.getMessage() + "]");
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<>();
                    params.put("year",year);

                    return params;
                }
            };

            queue.add(request);

            //RequestQueue queue = Volley.newRequestQueue(getContext());

        }
    }

    public void drawGraph(String response)
    {
        try{
            jsonArray = new JSONArray(response);
            list_cnt = jsonArray.length();

            avgmonthtem = new float[list_cnt];
            avgmonthhum = new float[list_cnt];
            avgmonthppm = new float[list_cnt];

            for (int i = 0; i < list_cnt-1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                avgmonthtem[i] = Float.parseFloat(jsonObject.getString("monthavgtem"));
                avgmonthhum[i] = Float.parseFloat(jsonObject.getString("monthavghum"));
                avgmonthppm[i] = Float.parseFloat(jsonObject.getString("monthavgppm"));

            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

        yearXAxisFormatter = new YearXAxisFormatter();


        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        barChart.invalidate();
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");


        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<12; i++)
        {
            entries.add(new BarEntry(i, avgmonthtem[i] ));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "온도(℃)");
        barDataSet.setBarBorderColor(Color.parseColor("#FF6C6C"));
        barDataSet.setColor(Color.parseColor("#FF6C6C"));

        BarData lineData = new BarData(barDataSet);

        barChart.setData(lineData);
        //mChart.setData(generateBarData(1, 20000, 12));

        Legend l = barChart.getLegend();
        l.setTypeface(tf);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setGranularity(1f);

        barChart.getAxisRight().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(yearXAxisFormatter);
        //월별 다 표시해줌
        xAxis.setLabelCount(12);


        //Graph 2
        barChart2.getDescription().setEnabled(false);
        barChart2.setDrawGridBackground(false);
        barChart2.setDrawBarShadow(false);
        barChart2.animateY(2000, Easing.EasingOption.EaseInCubic);
        barChart2.invalidate();

        //데이터 대입
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for(int i=0; i<12; i++)
        {
            entries2.add(new BarEntry(i, avgmonthhum[i] ));
        }

        BarDataSet barDataSet2 = new BarDataSet(entries2, "습도(%)");
        barDataSet2.setBarBorderColor(Color.parseColor("#5AAEFF"));
        barDataSet2.setColor(Color.parseColor("#5AAEFF"));

        BarData lineData2 = new BarData(barDataSet2);

        barChart2.setData(lineData2);
        //mChart.setData(generateBarData(1, 20000, 12));

        Legend l2 = barChart2.getLegend();
        l2.setTypeface(tf);

        YAxis leftAxis2 = barChart2.getAxisLeft();
        leftAxis2.setTypeface(tf);
        leftAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis2.setGranularity(1f);

        barChart2.getAxisRight().setEnabled(false);

        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setEnabled(true);
        xAxis2.setValueFormatter(yearXAxisFormatter);
        //월별 다 표시해줌
        xAxis2.setLabelCount(12);



        //Graph 3
        barChart3.getDescription().setEnabled(false);
        barChart3.animateY(2000, Easing.EasingOption.EaseInCubic);
        barChart3.invalidate();
        barChart3.setDrawGridBackground(false);
        barChart3.setDrawBarShadow(false);

        ArrayList<BarEntry> entries3 = new ArrayList<>();
        for(int i=0; i<12; i++)
        {
            entries3.add(new BarEntry(i, avgmonthppm[i] ));
        }

        BarDataSet barDataSet3 = new BarDataSet(entries3, "이산화탄소(0.1ppm)");
        barDataSet3.setBarBorderColor(Color.parseColor("#989898"));
        barDataSet3.setColor(Color.parseColor("#989898"));

        BarData lineData3 = new BarData(barDataSet3);

        barChart3.setData(lineData3);

        Legend l3 = barChart3.getLegend();
        l3.setTypeface(tf);

        YAxis leftAxis3 = barChart3.getAxisLeft();
        leftAxis3.setTypeface(tf);
        leftAxis3.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis3.setGranularity(1f);

        barChart3.getAxisRight().setEnabled(false);

        XAxis xAxis3 = barChart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setEnabled(true);
        xAxis3.setValueFormatter(yearXAxisFormatter);
        //월별 다 표시해줌
        xAxis3.setLabelCount(12);


    } //end of drawGraph

} // end of MonthGraph
