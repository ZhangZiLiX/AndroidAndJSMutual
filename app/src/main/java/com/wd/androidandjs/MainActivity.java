package com.wd.androidandjs;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * android 和 js 交互Demo
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webview;
    /**
     * android调用本地JS代码
     */
    private Button btnAlertAndroidCallJs;
    private TextView txtAcalljs;
    /**
     * 提示弹框：android调用本地JS代码
     */
    private Button mBtnPromptAndroidcalljs;
    /**
     * 确认弹框：android调用本地JS代码
     */
    private Button mBtnConfirmAndroidcalljs;
    private int version;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 涉及知识点：
         *
         *   一.Android通过WebView调用JS  方法有两种
         *      1. loadUrl();
         *      2. evaluateJavascript()
         *
         *   二.JS调用Android方法有三种：
         *      1.通过WebView的addJavascriptInterface() 进行对象映射
         *      2.通过WebViewClient的shouldOverrideUrlLoading()方法回调，拦截url
         *      3.通过WebChromeClient的:
         *                           onJsAlert() 警告框(WebView上alert无效，需要定制WebChromeClient处理弹出)
         *                           onJsConfirm() 确认弹框
         *                           onJsPrompt()  提示弹框
         *            方法回调拦截JS对：alert()
         *                           confirm()
         *                           prompt()消息
         * */


        /**
         * 一.android通过WebView调用JS实现
         *
         *   步骤：
         *       1.将需要调用的JS代码以.html格式放到src/main/assets文件夹里。如下
         *          // 文本名：javascript
         *         <!DOCTYPE html>
         *         <html>
         *
         *            <head>
         *                <meta charset="utf-8">
         *                <title>Carson_Ho</title>
         *
         *               // JS代码
         *               <script>
         *                 // Android需要调用的方法
         *                 function callJS(){
         *                   alert("Android调用了JS的callJS方法");
         *                  }
         *               </script>
         *
         *              </head>
         *
         *         </html>
         *
         *       2.在Android里通过WebView设置调用JS代码
         *           如下：
         *               setAndroidCallJsWebViews();
         *
         *
         *
         * 二.JS通过WebView调用Android实现
         *
         *     步骤：
         *        1.将需要调用的JS代码以.html格式放到src/main/assets文件夹里。如下
                *   // 文本名：javascript
         *          <!DOCTYPE html>
         *          <html>
         *             <head>
         *                <meta charset="utf-8">
         *                <title>Carson_Ho</title>
         *
         *                // JS代码
         *                <!-- 编写JS 要调用android的方法-->
         *                function callClassAndroid1(){
         *                    <!-- //第一种方法通过对象映射-->
         *                    // 由于对象映射，所以调用test对象等于调用Android映射的对象
         *                   Test.hello("JS使用第一种方法addJavaScriptInterface()调用andrioid的测试方法Test");
         *
         *                 }
         *
         *               </head>
         *
         *                <body onload="javascript:contact.showcontacts()">
         *                   //1 通过第一种方法  js调用Android
         *                    <button id="button1" title="JsCallAndroid1" type="button" "callAndroid()"></button>
         *
         *
         *                    <button id="button" onclick = "javascript:contact.toast('123')">haha</button>
         *                       <table border="0" width="100%" id="personTable" cellspacing="0">
         *                         <tr>
         *                            <td width="30%">姓名</td>
         *                            <td width="30%" align="center">存款</td>
         *                            <td align="center">电话</td>
         *                         </tr>
         *                       </table>
         *               </body>
         *
         *             </html>
         *
         *          2.定义一个与JS对象映射关系Android类 AndroidtoJs
         *
         *            // 继承自Object类
         *            public class AndroidtoJs extends Object {
         *
         *               // 定义JS需要调用的方法
         *               // 被JS调用的方法必须加入@JavascriptInterface注解
         *               @JavascriptInterface
         *               public void hello(String msg) {
         *                     System.out.println("JS调用了Android的hello方法");
         *               }
         *            }
         *
         *          3.在Android里通过WebView设置JS调用Android代码
         *             如下：
         *                   setJsCallAndroidWebViews();
         * */

        //1 初始化控件
        initView();


    }

    //1 初始化控件
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        //获取版本号
        version = Build.VERSION.SDK_INT;

        webview = (WebView) findViewById(R.id.webview);
        btnAlertAndroidCallJs = (Button) findViewById(R.id.btn_alert_androidcalljs);
        btnAlertAndroidCallJs.setOnClickListener(this);
        txtAcalljs = (TextView) findViewById(R.id.txt_acalljs);
        mBtnPromptAndroidcalljs = (Button) findViewById(R.id.btn_prompt_androidcalljs);
        mBtnPromptAndroidcalljs.setOnClickListener(this);
        mBtnConfirmAndroidcalljs = (Button) findViewById(R.id.btn_confirm_androidcalljs);
        mBtnConfirmAndroidcalljs.setOnClickListener(this);

        //1.1 设置WebView设置  android调用js方法
        setAndroidCallJsWebViews();

        //1.2 设置webview  JS调用Android
        setJsCallAndroidWebViews();
    }

    /**
     * 1.2 设置WebView设置
     *
     *   JS调用Android
     */
    private void setJsCallAndroidWebViews() {
        WebSettings websettings = webview.getSettings();//拿到设置
        websettings.setJavaScriptEnabled(true);//支持JavaScript与android交互
        //websettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置允许JS弹窗

        //通过addJavascriptInterface()将java对象映射到JS对象
        //参数1 android定义反射的类名
        //参数2 JS也就是在html中定义的对象名
        webview.addJavascriptInterface(new AndroidToJs(),"Test");

        //加载网页
        webview.loadUrl("file:///android_asset/javascript.html");

    }

    /**
     * 1.1 设置WebView设置
     *
     *   android调用js方法
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAndroidCallJsWebViews() {
        WebSettings websettings = webview.getSettings();//拿到设置
        websettings.setJavaScriptEnabled(true);//支持JavaScript与android交互
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置允许JS弹窗

        //1.2 先加入JS代码  也就是本地HTML网页
        /*
         * 格式规定：file:///android_asset/本地文件名.html
         * */
        webview.loadUrl("file:///android_asset/javascript.html");

        //1.3 设置点击事件
        //由于设置了弹框 需要取消掉弹框  借用辅助类WevChromeClient进行设置
        webview.setWebChromeClient(new WebChromeClient() {
            // 确定弹框
            @Override
            public boolean onJsAlert(WebView view, final String url, final String message, final JsResult result) {
                //创建一个弹框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //设置弹框
                builder.setTitle("警告：callAlertJS()方法");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击后取消弹框 result是消息框的返回值
                        result.confirm(); //alert弹框没有返回值
                        Toast.makeText(MainActivity.this, ":" + message, Toast.LENGTH_LONG).show();
                    }
                });
                //设置
                //builder.setCancelable(false);
                builder.show();//展示弹框
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, final String message, String defaultValue, final JsPromptResult result) {
                //创建一个弹框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //设置弹框
                builder.setTitle("提示：callPromptJS()方法");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击后取消弹框
                        //注意： 提示弹框Prompt有多个返回值
                             //      点击 确认 返回 输入框中的值
                             //      点击 取消 返回 null
                        result.confirm("调用了js提示弹框");
                        Toast.makeText(MainActivity.this, ":" + message, Toast.LENGTH_LONG).show();
                    }
                });
                //设置
                //builder.setCancelable(false);
                builder.show();//展示弹框

                return true;
            }
        });


       /**
        *
        *
        * */
       webview.setWebChromeClient(new WebChromeClient(){
           @Override
           public boolean onJsConfirm(WebView view, String url, final String message, final JsResult result) {
               //创建一个弹框
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               //设置弹框
               builder.setTitle("提示：callAlertJS()方法");
               builder.setMessage(message);
               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //点击后取消弹框
                       //注意： Confirm有两个返回值
                       //         boolean： true:代表点击了确认   false:代表点击了取消
                       JsResult result1 = result;
                       result.confirm();
                       Toast.makeText(MainActivity.this, ":" + result1, Toast.LENGTH_LONG).show();
                   }
               });
               //设置
               //builder.setCancelable(false);
               builder.show();//展示弹框
               return true;
           }
       });


    }

    /**
     * 点击事件
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_alert_androidcalljs://点击android按钮 警告弹框 调用本地html文件
                //  Toast.makeText(this,"安卓调用JS",Toast.LENGTH_LONG).show();;
                //通过handler发送消息
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        //调用javascript的callJS()方法
                        //注意：调用的JS方法名要对应上
                        webview.loadUrl("javascript:callAlertJS()");
                    }
                });
                break;
            case R.id.btn_prompt_androidcalljs: //提示弹框  android调用JS
                //使用handler发送
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                      //加载本地网页
                        if(version<18){
                            //webview.loadUrl("javascript:callPromptJS()");
                        }else{

                            webview.evaluateJavascript("javascript:callPromptJS()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(final String value) {
                                    //此处为js返回结果
                                    //创建一个弹框
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    //设置弹框
                                    builder.setTitle("提示：callAlertJS()方法");
                                    builder.setMessage(value);
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //点击后取消弹框
                                            //result.confirm();
                                            Toast.makeText(MainActivity.this, ":" + value, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    //设置
                                    //builder.setCancelable(false);
                                    builder.show();//展示弹框
                                }
                            });

                        }

                    }
                });

                /**
                 * android调用JS方法二 evaluateJavascript
                 *
                 *  参数一：JS的方法
                 *
                 *  参数二：回调
                 *
                 * */


                break;
            case R.id.btn_confirm_androidcalljs://确认弹框调用
                //使用handler发送
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        //加载本地网页
                        webview.loadUrl("javascript:callConfimJS()");
                    }
                });
                break;
        }
    }
}
