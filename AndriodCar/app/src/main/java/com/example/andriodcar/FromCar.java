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
import android.widget.Toast;

import com.example.andriodcar.Bean.PakingSpace;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FromCar extends AppCompatActivity {

    Button btn_publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消顶部导航栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_from_car);
        btn_publish = findViewById(R.id.publish);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishFrom(view);
            }
        });
    }

    /**
     * 空闲车位表单发布
     * @param view
     */
    public void PublishFrom(View view){
        switch (view.getId()){
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
                EditText editText1 = findViewById(R.id.carNumber);
                EditText editText2 = findViewById(R.id.phoneNumber);
                EditText editText5 = findViewById(R.id.moneyCar);

                Spinner startTimeHourSpinner = findViewById(R.id.StarttimeHour);
                Spinner startTimeMinSpinner = findViewById(R.id.StarttimeMin);
                Spinner endTimeHourSpinner = findViewById(R.id.EndtimeHour);
                Spinner endTimeMinSpinner = findViewById(R.id.EndtimeMin);

                List<String> HourList = new ArrayList<>();
                for(int i=1;i<=24;i++){
                    HourList.add(String.valueOf(i));
                }
                List<String> MinList = new ArrayList<>();
                for(int i=1;i<=60;i++){
                    MinList.add(String.valueOf(i));
                }

                //适配器
                ArrayAdapter<String> HourListadapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, HourList);
                //设置样式
                HourListadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ArrayAdapter<String> MinListadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MinList);
                MinListadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                startTimeHourSpinner.setAdapter(HourListadapter);
                endTimeHourSpinner.setAdapter(HourListadapter);

                startTimeMinSpinner.setAdapter(MinListadapter);
                endTimeMinSpinner.setAdapter(MinListadapter);

                //获取表单数据
                String  carNumber = editText1.getText().toString();
                String  phoneNumber = editText2.getText().toString();
                String  startTime = "";
                String  endTime = "00:00";
                String  moneyCar = editText5.getText().toString();
                if(carNumber.equals("")|| phoneNumber.equals("") || moneyCar.equals("")){
                    Toast.makeText(this,"请填写完整的信息",Toast.LENGTH_SHORT).show();
                }else {
                    /**
                     * 这一部分是数据处理
                     * 接下来存入数据
                     * 考虑是利用bean实例化对应的对象，利用json传递
                     *
                     */
                    PakingSpace pakingSpace = new PakingSpace();
                    pakingSpace.setParkSpaceNum(carNumber);
                    pakingSpace.setPhone_number(phoneNumber);
                    pakingSpace.setStartYime(startTime);
                    pakingSpace.setEndingTime(endTime);
                    pakingSpace.setFirstThreeTime(moneyCar);
                    //这两个默认设置为1
                    pakingSpace.setThreeAndSixTime("1");
                    pakingSpace.setSixAndTwenty("1");
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
