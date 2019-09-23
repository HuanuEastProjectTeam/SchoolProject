package utils;

import com.example.andriodcar.Bean.Userbean;

import org.json.JSONArray;
import org.json.JSONObject;

public class javautils {

    public static Userbean parseJSONWithJSONObject(String JsonData) {
       Userbean bean=new Userbean();
        try
        {
            JSONArray jsonArray = new JSONArray(JsonData);

                JSONObject jsonObject = jsonArray.toJSONObject(jsonArray);
                String uid = jsonObject.getString("uid");
                String upwd = jsonObject.getString("upwd");
                String uname = jsonObject.getString("uname");
                String umoney = jsonObject.getString("umoney");
                bean.setUid(uid);
                bean.setUpwd(upwd);
                bean.setUname(uname);
                bean.setMoney(Double.parseDouble(umoney));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return bean;
    }

}
