package com.mike_milk.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.mike_milk.myapplication1.httprequesthelper.Callback;
import com.mike_milk.myapplication1.httprequesthelper.NetUtil;
import com.mike_milk.myapplication1.httprequesthelper.Request;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String key [] = {"username","password"};
        String value [] = {"700","666"};
        Request request = new Request.Builder("http://bihu.jay86.com/login.php").Method("POST")
                .key(key)
                .value(value).builder();

        NetUtil.getInstance().execute(request, new Callback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onFailed(Throwable t) {
                t.printStackTrace();
            }
        });
    }
    }

