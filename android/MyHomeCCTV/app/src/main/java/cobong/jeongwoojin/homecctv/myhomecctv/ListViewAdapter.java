package cobong.jeongwoojin.homecctv.myhomecctv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JEONGWOOJIN on 2018-01-25.
 */

public class ListViewAdapter extends BaseAdapter
{
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    private ViewHolder viewHolder2;
    private List<String> list;
    private List<String> list2;

    public int getCount()
    {
        return list.size();
    }
    public Object getItem(int i)
    {
        return null;
    }
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = inflate.inflate(R.layout.data_listitem,null);
            viewHolder = new ViewHolder();
            viewHolder.idx = (TextView) view.findViewById(R.id.idx);
            viewHolder.data = (TextView) view.findViewById(R.id.data);
            view.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.idx.setText( list2.get(i) );
        viewHolder.data.setText( list.get(i) );
        return view;
    }

    ListViewAdapter(Context context, List<String> list,List<String> list2)
    {
        this.list = list;
        this.list2 = list2;
        this.inflate = LayoutInflater.from(context);
    }

    class ViewHolder{
        public TextView idx;
        public TextView data;
    }
}
