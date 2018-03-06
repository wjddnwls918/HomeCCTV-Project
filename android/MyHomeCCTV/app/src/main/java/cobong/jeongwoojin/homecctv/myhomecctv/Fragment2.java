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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class Fragment2 extends Fragment implements AbsListView.OnScrollListener {

    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private List<String> list2;
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    JSONArray jsonArray;
    int list_cnt = 0 ;
    String getinputtime[];

    EditText searchtext;
    Button search;
    String transdata;
    int curPnt;

    public Fragment2()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment2,container,false);


        listView = (ListView) viewGroup.findViewById(R.id.listview);

        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        adapter = new ListViewAdapter(getContext(), list,list2);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(this);
        getItem();

        searchtext = (EditText)viewGroup.findViewById(R.id.searchtext);
        search = (Button)viewGroup.findViewById(R.id.search);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = list.get(i);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentManager.beginTransaction();
                //Log.d("data check",data);
                fragmentTransaction.replace(R.id.fragment_container,MonthGraph.newInstance(data));
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });



        return viewGroup;
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
        String url = "http://192.168.219.136/MonthResult.php";
        //String url = "http://49.161.122.232:8888/MonthResult.php";

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

                        for(int i = 0; i <list_cnt; i++)
                        {
                                if(i > list_cnt-1)
                                    break;

                                list.add(getinputtime[i]);
                                list2.add(Integer.toString(i + 1));

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

                mLockListView = false;
            }
        },1000);

    }



}
