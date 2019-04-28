# AndroidAndJSMutual
简单实现，安卓调用JS
安卓与JS相互调用：
      
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
         
本地HTML网页代码编写案例：
    1.存储位置： src/main/assets文件夹下，创建html文件 javascript.html
    2.代码如下：
         <!DOCTYPE html>
<html>

  <head>
      <meta charset="utf-8">
      <title>安卓端编写的本地网页</title>

      <script>
         <!-- 编写android要调用JS的方法-->
          function callAlertJS(){
             <!--警告弹框-->
             alert("Android调用了JS的callJS方法");

          }

          function callPromptJS(){
             <!--提示弹框-->
             alert("Android调用了提示弹框方法");
          }

          function callConfimJS(){
             <!--确定弹框-->
             alert("Android调用了确定弹框方法");
          }


          <!-- 编写JS 要调用android的方法-->
          function callClassAndroid1(){
            <!-- //第一种方法通过对象映射-->
             // 由于对象映射，所以调用test对象等于调用Android映射的对象
             Test.hello("JS使用第一种方法addJavaScriptInterface()调用andrioid的测试方法Test");

          }

       </script>

  </head>



  <body>
     <!--//1 通过第一种方法  js调用Android
         注意： 这里的onclick 点击事件，是你在上面定义的函数名
     -->
     <button type="button" id="button1" onclick="callClassAndroid1()">JsCallAndroid1</button>


      <button id="button" onclick = "javascript:contact.toast('123')">haha</button>
      <table border="0" width="100%" id="personTable" cellspacing="0">
          <tr>
              <td width="30%">姓名</td>
              <td width="30%" align="center">存款</td>
              <td align="center">电话</td>
          </tr>
      </table>
  </body>


</html>
  
参考资料：
     https://www.jianshu.com/p/74a6db560be2
