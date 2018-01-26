package com.example.jeongwoojin.homecctv;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JEONGWOOJIN on 2018-01-26.
 */

public class Fragment1 extends Fragment implements AbsListView.OnScrollListener {

    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private List<String> list2;
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    JSONObject json = null;
    JSONArray jsonArray;

    int list_cnt = 0 ;
    String getIdx[];
    String getTemperature[];
    String getHumidity[];
    String getflameState[];
    String gethumStata[];
    String getinputtime[];

    Spinner spinner;

    public Fragment1()
    {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment1,container,false);


        listView = (ListView) viewGroup.findViewById(R.id.listview);
        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progressbar);
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        adapter = new ListViewAdapter(getContext(), list,list2);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);
        getItem();

        spinner = (Spinner) viewGroup.findViewById(R.id.spinner);
        //input array data
        ArrayList<String> check = new ArrayList<>();
        check.add("1");
        check.add("2");
        check.add("4");
        check.add("5");

        String[] dropdown = new String[2];
        dropdown[0] = "날짜";
        dropdown[1] = "??";

        //using ArrayAdapter
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, dropdown);
        spinner.setAdapter(spinnerAdapter);

        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return viewGroup;

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (i2 > 0) && (i + i1 >= i2);
    }

    private void getItem()
    {
        mLockListView = true;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.219.136/DayCheck.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                //요청 성공시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", "[" + response + "]");
                        Bundle bundle = new Bundle();
                        //버스로 넘김
                        //BusProvider.getInstance().post(response);
                        try {
                            jsonArray = new JSONArray(response);
                            list_cnt = jsonArray.length();

                            getIdx = new String[list_cnt];
                            getTemperature = new String[list_cnt];
                            getHumidity = new String[list_cnt];
                            getflameState = new String[list_cnt];
                            gethumStata = new String[list_cnt];
                            getinputtime = new String[list_cnt];

                            for (int i = 0; i < list_cnt; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //getIdx[i] = jsonObject.getString("idx");
                                getinputtime[i] = jsonObject.getString("inputtime");
                                Log.d("getIdx, getinputtime ",getinputtime[i]);
                            }
                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Log.d("jsonArray",""+jsonArray);

                        for(int i = 0; i <20; i++)
                        {
                            Log.d("list_cnt" , Integer.toString(list_cnt));
                            if( i >= list_cnt)
                                break;

                            // Log.d("success",getinputtime[i]);
                            //
                            // String label = "Label " + ((page * OFFSET) + i);
                            //list.add(label);
                            list.add(getinputtime[(page * OFFSET) + i]);
                            list2.add( Integer.toString(( page*OFFSET ) + i+1) );
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "[" + error.getMessage() + "]");
                    }
                }
        );

        queue.add(request);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);

    }

}
