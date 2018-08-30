package xxm.com.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewCalendar extends LinearLayout {

    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    private Calendar curDate = Calendar.getInstance();

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context){
        //绑定控件
        bindControl(context);
        //绑定事件
        bindControlEvent();
        //初始化时渲染
        renderCalendar();
    }

    private void bindControl(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view,this);

        btnPrev = (ImageView)findViewById(R.id.btnPrev);
        btnNext = (ImageView)findViewById(R.id.btnNext);
        txtDate = (TextView)findViewById(R.id.txtDate);
        grid = (GridView)findViewById(R.id.calendar_grid);
    }

    private void bindControlEvent(){
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.set(Calendar.MONTH,-1);
                renderCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.set(Calendar.MONTH,1);
                renderCalendar();
            }
        });

    }

    private void renderCalendar(){
        //格式化标题
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtDate.setText(sdf.format(curDate.getTime()));
        //计算表格
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH,1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1;
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);

        int maxCellCount = 6*7;
        while (cells.size() < maxCellCount){
            cells.add(calendar.getTime());
            //表格加一，同时前进一天
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        grid.setAdapter(new CalendarAdapter(getContext(),cells));
    }


    private  class CalendarAdapter extends ArrayAdapter<Date>{

        LayoutInflater inflater;
        public CalendarAdapter(@NonNull Context context , ArrayList<Date> days) {
            super(context, R.layout.calendar_text_day, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);

            if(convertView==null){
                convertView = inflater.inflate(R.layout.calendar_text_day,parent,false);
            }

            int day = date.getDate();
            ((TextView)convertView).setText(String.valueOf(day));

            Date now = new Date();
            if(date.getMonth() == now.getMonth()){
                //当月
                ((TextView) convertView).setTextColor(Color.parseColor("#000000"));
            }else {
                //非当月
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));
            }

            if(now.getDate()==date.getDate() && now.getMonth()==date.getMonth() && now.getYear()==date.getYear()){
                //当天设置为红色
                ((TextView) convertView).setTextColor(Color.parseColor("#ff0000"));
            }
            return convertView;
        }
    }
}
