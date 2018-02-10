package cobong.jeongwoojin.homecctv.myhomecctv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.squareup.otto.Bus;


/**
 * Created by JEONGWOOJIN on 2018-02-02.
 */

public class DateDialog extends DialogFragment {

    DatePicker datePicker;

    Button ok;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.popup,container,false);

        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth(),Toast.LENGTH_LONG).show();
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                String stryear;
                String strmonth;
                String strday;

                if(year <10)
                {
                    stryear = "0"+Integer.toString(year);
                }
                else
                    stryear = Integer.toString(year);

                if(month <10)
                {
                    strmonth = "0"+Integer.toString(month);
                }
                else
                    strmonth = Integer.toString(month);

                if(day <10)
                {
                    strday = "0"+Integer.toString(day);
                }
                else
                    strday = Integer.toString(day);


                //Log.d("day : ",stryear+"-"+strmonth+"-"+strday);

                BusProvider.getInstance().post(new String(stryear+"-"+strmonth+"-"+strday));

                dismiss();


            }
        });

        return view;
    }





    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }
}

class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}



