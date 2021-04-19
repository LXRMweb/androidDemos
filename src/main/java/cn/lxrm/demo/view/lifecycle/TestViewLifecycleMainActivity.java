package cn.lxrm.demo.view.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appdemo01.R;

public class TestViewLifecycleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view_lifecycle_main);
    }
}