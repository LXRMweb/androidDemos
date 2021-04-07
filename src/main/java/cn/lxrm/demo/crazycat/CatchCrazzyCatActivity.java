package cn.lxrm.demo.crazycat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CatchCrazzyCatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Playground(this));
    }
}