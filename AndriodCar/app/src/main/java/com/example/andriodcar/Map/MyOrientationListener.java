package com.example.andriodcar.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.text.AttributedCharacterIterator;

/**
 * 方向传感器实现类
 */
public class MyOrientationListener implements SensorEventListener {


    /**
     * 方向传感器设置监听
     */
    private SensorManager sensorManager;//传感器总管家
    private Context context;//上下文对象
    private Sensor sensor;//传感器对象
    private float[] accelerometerValues =new float[3];//磁场加速度传感器的值
    private float[] magneticValues =new float[3];//磁场传感器的值
    private float lastX;
    private String DAYIN="log";
    public MyOrientationListener(Context context){
        super();
        this.context=context;
        Log.i(DAYIN,"进入实现构造方法");
        start();//调用传感器注册方法
    }


    /**
     * 监听开始实现方法
     */
    public void start(){
        //获取系统传感器管家
        sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager!=null){//判断主机是否支持传感器
           sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME );//为磁场传感器注册监听器
           sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME );//为加速度传感器注册监听器
        }

    }

    /**
     * 监听结束实现方法
     */
    public void stop(){
       sensorManager.unregisterListener(this);//取消注册的加速度，磁场传感器

    }

    /**
     * 传感器的值发生改变时触发方法
     * @param event 传感器事件对象
     */
    public void onSensorChanged(SensorEvent event){

        Log.i(DAYIN,"开始测算");
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){//判断为加速度传感器的值改变
            accelerometerValues=event.values.clone();//把传感器改变的值传给加速度变量

        }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){//判断为磁场传感器
            magneticValues=event.values.clone();//获取磁场传感器的值

        }
        float[] R=new float[9];//保存旋转数据的数组
        float[] values=new float[3];//保存方向数据
        SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticValues);//获得返回的一个包含旋转矩阵的R数组
        SensorManager.getOrientation(R,values);//获取方向值
        float xAngle= (float) Math.toDegrees(values[0]);//x轴方向值赋值
        if(xAngle<0){
            xAngle+=360;//旋转值
        }
        xAngle=xAngle/5*5;//以航向角5°为幅度
        if(onOrientationListener!=null){
            Log.i(DAYIN,"传递方向值");
            onOrientationListener.onOrientationChanged((xAngle));//调用接口方法，传递方向值
        }
        Log.i(DAYIN,"调用注销传感器方法");
       // stop();//消除注册传感器
      //  lastX=xAngle;//赋值给全局变量
    }

    /**
     * 传感器精度变化触发方法
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    /**
     * 设置回调接口
     */
    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }
    private OnOrientationListener onOrientationListener;//创建接口对象

    public void setOnOrientationListener(OnOrientationListener onOrientationListener){
        this.onOrientationListener=onOrientationListener;
    }


}
