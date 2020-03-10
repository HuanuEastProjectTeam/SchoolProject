package com.example.andriodcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class FromCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消顶部导航栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_from_car);
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
                 * city 城市
                 * carNumber 车位号
                 * phoneNumber 联系电话
                 * startTime 车位空闲开始时间
                 * endTime 车位空闲结束时间
                 * moneyCar 收费标准
                 */
                EditText editText = findViewById(R.id.addressCar);
                EditText editText1 = findViewById(R.id.carNumber);
                EditText editText2 = findViewById(R.id.phoneNumber);
                EditText editText3 = findViewById(R.id.startTime);
                EditText editText4 = findViewById(R.id.endTime);
                EditText editText5 = findViewById(R.id.moneyCar);

                //获取表单数据
                String city = editText.getText().toString();
                String  carNumber = editText.getText().toString();
                String  phoneNumber = editText.getText().toString();
                String  startTime = editText.getText().toString();
                String  endTime = editText.getText().toString();
                String  moneyCar = editText.getText().toString();
                if(city.equals("") || carNumber.equals("")|| phoneNumber.equals("") || startTime.equals("") || endTime.equals("") || moneyCar.equals("")){
                    Toast.makeText(this,"请填写完整的信息",Toast.LENGTH_SHORT).show();
                }else {
                    /**
                     * 这一部分是数据处理
                     * 接下来存入数据
                     * 考虑是利用bean实例化对应的对象，利用json传递
                     *
                     */
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
