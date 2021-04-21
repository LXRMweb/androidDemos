package cn.lxrm.demo.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appdemo01.R;

public class TestThreadsMainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    Button buttonAsyncTask;
    Button buttonPrimitive;
    SubThreadDemoBasicThreadAPI subThreadDemoBasicThreadAPI;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_threads_main);

        textView = (TextView) findViewById(R.id.textView);
        buttonAsyncTask = (Button) findViewById(R.id.button_assycTask);
        buttonPrimitive = (Button) findViewById(R.id.button_primitive);

        buttonAsyncTask.setOnClickListener(this);
        buttonPrimitive.setOnClickListener(this);

        subThreadDemoBasicThreadAPI = new SubThreadDemoBasicThreadAPI(textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_assycTask:
                break;
            case R.id.button_primitive:
                subThreadDemoBasicThreadAPI.subThreadWithprimitiveAPI();
                break;
            default:
                break;
        }
    }
}