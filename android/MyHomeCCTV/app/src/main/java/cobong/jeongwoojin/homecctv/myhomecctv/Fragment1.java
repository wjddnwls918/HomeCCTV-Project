package cobong.jeongwoojin.homecctv.myhomecctv;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by JEONGWOOJIN on 2018-01-26.
 */

public class Fragment1 extends Fragment implements AbsListView.OnScrollListener {

    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private List<String> list2;
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page;                           // 페이징변수. 초기 값은 0 이다.
    private int OFFSET;                // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    JSONArray jsonArray;
    int list_cnt = 0 ;
    String getinputtime[];
    Spinner spinner;

    EditText searchtext;
    Button search;
    String transdata;
    int curPnt;

    public Fragment1()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment1,container,false);
        listView = (ListView) viewGroup.findViewById(R.id.listview);
        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progressbar);
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        adapter = new ListViewAdapter(getContext(), list,list2);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);

        page = 0;
        OFFSET = 20;
        getItem();

        searchtext = (EditText)viewGroup.findViewById(R.id.searchtext);
        search = (Button)viewGroup.findViewById(R.id.search);


        //리스트뷰 아이템 선택
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = list.get(i);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,DayGraph.newInstance(data));
                //backstack 설정
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        spinner = (Spinner) viewGroup.findViewById(R.id.spinner);
        //input array data

        String[] dropdown = new String[2];
        dropdown[0] = "번호";
        dropdown[1] = "날짜";

        //using ArrayAdapter
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, dropdown);

        spinner.setAdapter(spinnerAdapter);

        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(),"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

                //Log.d("position check",Integer.toString(position));
                if(spinner.getItemIdAtPosition(position)==0)
                {
                    curPnt=0;

                    searchtext.setText("");
                    searchtext.setInputType(TYPE_CLASS_NUMBER);
                    searchtext.setHint("input index number");
                }
                else
                {
                    curPnt=1;

                    searchtext.setText("");
                    searchtext.setInputType(TYPE_CLASS_DATETIME);
                    searchtext.setHint("input date");

                    FragmentManager fm = getFragmentManager();
                    DateDialog dialog = new DateDialog();
                    dialog.show(fm,"select date");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //자료 검색
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(curPnt == 0) {

                    if(Integer.parseInt(searchtext.getText().toString()) > list_cnt || Integer.parseInt(searchtext.getText().toString()) <= 0)
                        transdata="0";
                    else
                        transdata = list.get(Integer.parseInt(searchtext.getText().toString()) - 1);

                }
                   else if(curPnt == 1)
                    transdata = searchtext.getText().toString();

                if( !list.contains(transdata) )
                    Toast.makeText(getContext(),"자료를 찾을 수 없습니다.",Toast.LENGTH_LONG).show();
                else {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, DayGraph.newInstance(transdata));
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

        return viewGroup;

    }

    @Subscribe
    public void result(String date)
    {
        searchtext.setText(date);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
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
        String url = "http://192.168.219.136/DayResult.php";
        //String url = "http://49.161.122.232:8888/DayResult.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                //요청 성공시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle = new Bundle();
                        try {
                            jsonArray = new JSONArray(response);
                            list_cnt = jsonArray.length();

                            getinputtime = new String[list_cnt];

                            for (int i = 0; i < list_cnt; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                getinputtime[i] = jsonObject.getString("inputtime");

                            }
                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                        for(int i = 0; i <20; i++)
                        {
                            if(i > list_cnt-1){
                                break;
                            }

                            if( ((page*OFFSET) + i) > list_cnt)
                                break;
                            else {

                                list.add(getinputtime[(page * OFFSET) + i]);
                                list2.add(Integer.toString((page * OFFSET) + i + 1));
                            }
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

    public static Fragment1 newInstance(String param1) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString("day", param1);
        fragment.setArguments(args);
        return fragment;
    }

}

