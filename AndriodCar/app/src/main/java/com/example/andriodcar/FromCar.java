package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andriodcar.Bean.PakingSpace;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FromCar extends AppCompatActivity implements View.OnClickListener {

    Button btn_publish;
    EditText editText1;
    EditText editText2;
    EditText editText5;
    TimePicker startTimepicker;
    TimePicker endTimepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消顶部导航栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_from_car);
        btn_publish = findViewById(R.id.publish);
        btn_publish.setOnClickListener(this);
        editText1 = findViewById(R.id.carNumber);
        editText2 = findViewById(R.id.phoneNumber);
        editText5 = findViewById(R.id.moneyCar);

        //时间选择器数据处理部分
        startTimepicker = findViewById(R.id.StartTimePicker);
        endTimepicker = findViewById(R.id.EndTimePicker);
        //设置为24小时制
        startTimepicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        endTimepicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        //获取当前时间
        SimpleDateFormat formatter   = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        Date curDate =  new Date(System.currentTimeMillis());

        startTimepicker.setHour(curDate.getHours());
        startTimepicker.setMinute(curDate.getMinutes()+20);

        endTimepicker.setHour(curDate.getHours()+2);
        endTimepicker.setMinute(curDate.getMinutes()+20);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish :{
                //点击了发布表单按钮
                /**
                 * 获取表单信息
                 * carNumber 车位号
                 * phoneNumber 联系电话
                 * startTime 车位空闲开始时间
                 * endTime 车位空闲结束时间
                 * moneyCar 收费标准
                 */

                //获取表单数据
                final String  carNumber = editText1.getText().toString();
                final String  phoneNumber = editText2.getText().toString();
                final String  startTime = startTimepicker.getHour() + ":" + startTimepicker.getMinute();
                final String  endTime = endTimepicker.getHour() + ":" + endTimepicker.getMinute();
                final String  moneyCar = editText5.getText().toString();
                if(carNumber.equals("")|| phoneNumber.equals("") || moneyCar.equals("") ){
                    Toast.makeText(this,"请填写完整的信息",Toast.LENGTH_SHORT).show();
                }else {
                    /**
                     * 这一部分是数据处理
                     * 接下来存入数据
                     * 考虑是利用bean实例化对应的对象，利用json传递
                     *
                     */
                    MainActivity.threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            PakingSpace pakingSpace = new PakingSpace();
                            pakingSpace.setParkSpaceNum(carNumber);
                            pakingSpace.setPhone_number(phoneNumber);
                            pakingSpace.setStartYime(startTime);
                            pakingSpace.setEndingTime(endTime);
                            pakingSpace.setFirstThreeTime(moneyCar);
                            //这两个默认设置和之前的一样，以后再做细分
                            pakingSpace.setThreeAndSixTime(moneyCar);
                            pakingSpace.setSixAndTwenty(moneyCar);

                            Connect.getConncet().FormCar(pakingSpace);
                        }
                    });
                    //发布成功给个提示
                    Toast.makeText(this,"发布成功",Toast.LENGTH_SHORT).show();
                }



                break;
            }
            case R.id.fanhui :{
                //结束当前页面，返回上一个activity
                finish();
            }
            default:{
                break;
            }
        }
    }
}
