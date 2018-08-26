package com.itfitness.jsbridgetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

public class MainActivity extends AppCompatActivity {
    private EditText et;
    private Button bt;
    private BridgeWebView webview;
    private Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        bt = (Button) findViewById(R.id.bt);
        bt2 = (Button) findViewById(R.id.bt2);
        webview = (BridgeWebView) findViewById(R.id.webview);
        webview.setDefaultHandler(new DefaultHandler());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:///android_asset/test.html");
//      注册监听方法当js中调用callHandler方法时会调用此方法（handlerName必须和js中相同）
        webview.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("TAG", "js返回：" + data);
                //显示js传递给Android的消息
                Toast.makeText(MainActivity.this, "js返回:" + data, Toast.LENGTH_LONG).show();
                //Android返回给JS的消息
                function.onCallBack("我是js调用Android返回数据：" + et.getText().toString());
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              调用js中的方法（必须和js中的handlerName想同）
                webview.callHandler("functionInJs", "Android调用js66", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.e("TAG", "onCallBack:" + data);
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        webview.setDefaultHandler(new DefaultHandler(){
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                function.onCallBack("Android收到了默认的消息");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              发送信息给js,此处不需要配置handlerName
                webview.send(et.getText().toString().trim(), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
//                      接收js的回调数据
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
