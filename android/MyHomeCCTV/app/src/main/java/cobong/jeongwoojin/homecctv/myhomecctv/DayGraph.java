package cobong.jeongwoojin.homecctv.myhomecctv;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link DayGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    JSONArray jsonArray;

    int list_cnt;
/*    int getIdx[];*/
    float getavgtem[];
    float getavghum[];
    float getavgppm[];

    String mintem;
    String minhum;
    String minppm;
    String maxtem;
    String maxhum;
    String maxppm;
    String monthtem;
    String monthhum;
    String monthppm;
    JSONObject tablejson;

    TextView setmintem;
    TextView setminhum;
    TextView setminppm;

    TextView setmaxtem;
    TextView setmaxhum;
    TextView setmaxppm;

    TextView setmonthtem;
    TextView setmonthhum;
    TextView setmonthppm;

    TextView setavgtem;
    TextView setavghum;
    TextView setavgppm;

    float avgtem = 0;
    float avghum = 0;
    float avgppm = 0;

  /*  int getflameState[];
    int gethumStata[];
    String getinputtime[];
*/
    String day;

    GetDayData getDayData;


    TextView dayinfo;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LineChart lineChart;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static DayGraph newInstance(String param1, String param2) {
        DayGraph fragment = new DayGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static DayGraph newInstance(String param1) {
        DayGraph fragment = new DayGraph();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_day_graph, container, false);
        Log.v("Test","Param1 : "+mParam1 + " Param2 :"+mParam2);

        BusProvider.getInstance().register(this);

        day =mParam1;
        getDayData = new GetDayData();
        getDayData.start();



        lineChart = ( LineChart) viewGroup.findViewById(R.id.chart);

        dayinfo = (TextView)viewGroup.findViewById(R.id.dayinfo);
        dayinfo.setText(" ["+day+"] 그래프");

        setavgtem = (TextView)viewGroup.findViewById(R.id.avgtem);
        setavghum = (TextView)viewGroup.findViewById(R.id.avghum);
        setavgppm = (TextView)viewGroup.findViewById(R.id.avgppm);

        setmintem = (TextView)viewGroup.findViewById(R.id.mintem);
        setminhum= (TextView)viewGroup.findViewById(R.id.minhum);
        setminppm = (TextView)viewGroup.findViewById(R.id.minppm);

        setmaxtem = (TextView)viewGroup.findViewById(R.id.maxtem);
        setmaxhum = (TextView)viewGroup.findViewById(R.id.maxhum);
        setmaxppm = (TextView)viewGroup.findViewById(R.id.maxppm);

        setmonthtem = (TextView)viewGroup.findViewById(R.id.monthtem);
        setmonthhum = (TextView)viewGroup.findViewById(R.id.monthhum);
        setmonthppm = (TextView)viewGroup.findViewById(R.id.monthppm);


/*
        minMaxMonth = new MinMaxMonth();
        minMaxMonth.start();*/


        return viewGroup;
       // return inflater.inflate(R.layout.fragment_day_graph, container, false);
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }


    public class GetDayData extends Thread{

        @Override
        public void run() {
            super.run();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://192.168.219.136/Avgdata.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    //요청 성공시
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("jsonArray",""+jsonArray);
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
                    params.put("day",day);

                    return params;
                }
            };

            queue.add(request);
        }
    }


    //그래프 그리기
    public void drawGraph(String response)
    {
        //핸들러 선언
        Dayavghandler dayavghandler;

    try{
        jsonArray = new JSONArray(response);
        list_cnt = jsonArray.length();

        getavgtem = new float[list_cnt];
        getavghum = new float[list_cnt];
        getavgppm = new float[list_cnt];

        float tempcnt = 0;
        for (int i = 0; i < list_cnt-1; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            getavgtem[i] = Float.parseFloat(jsonObject.getString("avgtem"));
            getavghum[i] = Float.parseFloat(jsonObject.getString("avghum"));
            getavgppm[i] = Float.parseFloat(jsonObject.getString("avgppm"));

            if(getavgtem[i] == 0 || getavghum[i] == 0 || getavgppm[i] == 0) {

                continue;
            }
            else{
                avgtem += getavgtem[i];
                avghum += getavghum[i];
                avgppm += getavgppm[i];
                tempcnt+=1;
            }
        }//end of for

        avgtem /= (tempcnt);
        avghum /= (tempcnt);
        avgppm /= (tempcnt);

        JSONObject jsonObject = jsonArray.getJSONObject(list_cnt-1);
        mintem = jsonObject.getString("mintem");
        minhum = jsonObject.getString("minhum");
        minppm = jsonObject.getString("minppm");

        maxtem = jsonObject.getString("maxtem");
        maxhum = jsonObject.getString("maxhum");
        maxppm = jsonObject.getString("maxppm");

        monthtem = jsonObject.getString("monthtem");
        monthhum = jsonObject.getString("monthhum");
        monthppm = jsonObject.getString("monthppm");


        Bundle tem = new Bundle();
        tem.putString("avgtem",Double.toString(avgtem) );
        tem.putString("avghum",Double.toString(avghum));
        tem.putString("avgppm",Double.toString(avgppm));
        tem.putString("mintem",mintem);
        tem.putString("minhum",minhum);
        tem.putString("minppm",minppm);

        tem.putString("maxtem",maxtem);
        tem.putString("maxhum",maxhum);
        tem.putString("maxppm",maxppm);

        tem.putString("monthtem",monthtem);
        tem.putString("monthhum",monthhum);
        tem.putString("monthppm",monthppm);

        Message temmsg = new Message();
        temmsg.setData(tem);
        dayavghandler = new Dayavghandler();
        dayavghandler.handleMessage(temmsg);


    }catch(JSONException e)
    {
        e.printStackTrace();
    }
        //시간별 평균 데이터로 그래프를 그린다.
        //최저 최소값 표로 나타낸다.
        //첫번째 인자는 x, 두번째 인자는 y축.

        //온도
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0; i<list_cnt-1; i++)
        {
            entries.add(new Entry(i+1, getavgtem[i])  );
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "온도(℃)");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF4848"));
        lineDataSet.setCircleColorHole(Color.RED);
        lineDataSet.setColor(Color.parseColor("#FF4848"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        //습도
        ArrayList<Entry> entries2 = new ArrayList<>();
        for(int i=0; i<list_cnt-1; i++)
        {
            entries2.add(new Entry(i+1, getavghum[i]) );
        }

        LineDataSet lineDataSet2 = new LineDataSet(entries2, "습도(%)");
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setCircleColorHole(Color.BLUE);
        lineDataSet2.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(false);

        //이산화탄소
        ArrayList<Entry> entries3 = new ArrayList<>();
        for(int i=0; i<list_cnt-1; i++)
        {
            entries3.add(new Entry(i+1, getavgppm[i]) );
        }

        LineDataSet lineDataSet3 = new LineDataSet(entries3, "이산화탄소(0.1ppm)");
        lineDataSet3.setLineWidth(2);
        lineDataSet3.setCircleRadius(6);
        lineDataSet3.setCircleColor(Color.parseColor("#CFCFCF"));
        lineDataSet3.setCircleColorHole(Color.GRAY);
        lineDataSet3.setColor(Color.parseColor("#CFCFCF"));
        lineDataSet3.setDrawCircleHole(true);
        lineDataSet3.setDrawCircles(true);
        lineDataSet3.setDrawHorizontalHighlightIndicator(false);
        lineDataSet3.setDrawHighlightIndicators(false);
        lineDataSet3.setDrawValues(false);

        //종합
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(lineDataSet);
        sets.add(lineDataSet2);
        sets.add(lineDataSet3);

        //라인 그래프
        LineData lineData = new LineData(sets);

        //xml에 그래프 넣기
        lineChart.setData(lineData);

        //x축 y축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
    }

    class Dayavghandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            DecimalFormat format = new DecimalFormat();
            format.applyLocalizedPattern("0.00");
            Bundle bundle = msg.getData();

                setavgtem.setText(format.format(Float.parseFloat(bundle.getString("avgtem"))) );
                setavghum.setText(format.format(Float.parseFloat(bundle.getString("avghum"))));
                setavgppm.setText(format.format(Float.parseFloat(bundle.getString("avgppm"))));

            if(bundle.getString("mintem").equals("0") )
                setmintem.setText(bundle.getString("mintem")) ;
            else
                setmintem.setText(format.format(Float.parseFloat(bundle.getString("mintem"))));

            if(bundle.getString("minhum").equals("0")  )
                setminhum.setText(bundle.getString("minhum"));
            else
                setminhum.setText(format.format(Float.parseFloat(bundle.getString("minhum"))));

            if(bundle.getString("minppm").equals("0")  )
                setminppm.setText(bundle.getString("minppm"));
            else
                setminppm.setText(format.format(Float.parseFloat(bundle.getString("minppm"))));

            setmaxtem.setText( format.format(Float.parseFloat(bundle.getString("maxtem")))    );
            setmaxhum.setText( format.format(Float.parseFloat(bundle.getString("maxhum")))    );
            setmaxppm.setText( format.format(Float.parseFloat(bundle.getString("maxppm")))    );
            //Log.d("avg check",dayGraph.getAvgtem()+" "+dayGraph.getAvghum()+" "+dayGraph.getAvgppm());

            String temptem = Double.toString( avgtem - Double.parseDouble(bundle.getString("monthtem")) );
            String temphum = Double.toString( avghum - Double.parseDouble(bundle.getString("monthhum")) );
            String tempppm = Double.toString( avgppm - Double.parseDouble(bundle.getString("monthppm")) );

            if(Double.parseDouble(temptem) >0 )
                setmonthtem.setTextColor(Color.RED);
             else
                setmonthtem.setTextColor(Color.BLUE);

            if(Double.parseDouble(temphum) > 0)
                setmonthhum.setTextColor(Color.RED);
            else
                setmonthhum.setTextColor(Color.BLUE);

            if(Double.parseDouble(tempppm) > 0)
                setmonthppm.setTextColor(Color.RED);
            else
                setmonthppm.setTextColor(Color.BLUE);

            setmonthtem.setText(format.format(Float.parseFloat(temptem)));
            setmonthhum.setText(format.format(Float.parseFloat(temphum)));
            setmonthppm.setText(format.format(Float.parseFloat(tempppm)));

            MyMarkerView marker = new MyMarkerView(getContext(),R.layout.markerviewtext);
            marker.setChartView(lineChart);
            lineChart.setMarker(marker);

        }//end of handleMessage()
    }//end of handler
}
//end of onCreteView



