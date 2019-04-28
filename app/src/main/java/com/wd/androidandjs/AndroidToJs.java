package com.wd.androidandjs;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Author : 张自力
 * Created on time.
 *
 * 方式一  JS调用android的关系映射类
 *
 */
public class AndroidToJs extends Object{

    //定义JS需要调用的方法
    //被JS调用的方法必须加入@JavaScriptInterface注解
    @JavascriptInterface
    public void hello(String message){
        System.out.print("JS调用了Android的hello方法");
        Log.i("AndroidToJs1==hello:", "hello:"+message);
    }


}
