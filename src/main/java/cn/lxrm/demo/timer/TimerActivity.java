package cn.lxrm.demo.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdemo01.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputTime;
    Button getTimeBtn;
    Button startTimerBtn;
    Button stopTimerBtn;
    TextView time;
    Timer timer;
    TimerTask timerTask;
    int timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();
    }
    
    private void initView(){
        inputTime = findViewById(R.id.inputTime);
        getTimeBtn = findViewById(R.id.getTimeBtn);
        startTimerBtn = findViewById(R.id.startTimerBtn);
        stopTimerBtn = findViewById(R.id.stopTimeBtn);
        time = findViewById(R.id.time);

        // 给按钮添加点击事件
        getTimeBtn.setOnClickListener(this);
        startTimerBtn.setOnClickListener(this);
        stopTimerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getTimeBtn:
                String time = inputTime.getText().toString();
                if (time.equals("") || time.equals(null)) {
                    Toast.makeText(this, "请输入‘设置时间’",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this, time,Toast.LENGTH_LONG).show();
                this.time.setText(time);
                timeLeft = Integer.parseInt(time);
                break;
            case R.id.startTimerBtn:
                startTimer();
                break;
            case R.id.stopTimeBtn:
                stopTimer();
                break;
            default:
                Toast.makeText(this,"未知点击事件",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void stopTimer() {
        timer.cancel();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            time.setText(msg.arg1+"");
            startTimer();
        }
    };

    private void startTimer() {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                Message message = mHandler.obtainMessage();
                message.arg1 = timeLeft;
                mHandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000);
    }

}