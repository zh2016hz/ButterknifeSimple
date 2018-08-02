package com.xiaoniu.finance.butterknifesimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.button1)
    Button  mButton;
    @InjectView(R.id.button2)
    Button  mButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectViewUtils.inject(this);
        mButton.setText("我是第一个button");
    }

    @OnClick({R.id.button1,R.id.button2})
    private void  XXX(View view ){
        switch (view.getId()){
            case R.id.button1:
                Toast.makeText(this,"第一个按钮的手动编译时注解",Toast.LENGTH_LONG).show();
                break;
            case R.id.button2:
                Toast.makeText(this,"第222个按钮的手动编译时注解,主要用到反射",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
