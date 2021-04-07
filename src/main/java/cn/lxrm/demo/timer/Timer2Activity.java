package cn.lxrm.demo.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdemo01.R;

import java.util.Timer;
import java.util.TimerTask;

public class Timer2Activity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = this.getClass().getSimpleName();
    EditText timeInput2;
    Button getTimeBtn2;
    TextView timeLeft;
    Button startTimerBtn2;
    Button stopTimerBtn2;
    Button resetTimerBtn2;
    int currTimeLftInt;
    Timer timer;
    TimerTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);

        initView();
    }

    private void initView(){
        timeInput2 = findViewById(R.id.timeInput2);
        getTimeBtn2 = findViewById(R.id.getTimeBtn2);
        timeLeft = findViewById(R.id.timeLeft);
        startTimerBtn2 = findViewById(R.id.startTimerBtn2);
        stopTimerBtn2 = findViewById(R.id.stopTimerBtn2);
        resetTimerBtn2 = findViewById(R.id.resetTimerBtn2);

        getTimeBtn2.setOnClickListener(this);
        startTimerBtn2.setOnClickListener(this);
        stopTimerBtn2.setOnClickListener(this);
        resetTimerBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getTimeBtn2:
                Toast.makeText(this,"获取时间",Toast.LENGTH_LONG).show();
                String tmp = timeInput2.getText().toString();
                if (tmp.equals("") || tmp.equals(null)) {
                    Toast.makeText(this, "请先录入‘设置时间’",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this,tmp,Toast.LENGTH_LONG).show();
                timeLeft.setText(tmp);
                currTimeLftInt = Integer.parseInt(tmp);
                break;
            case R.id.startTimerBtn2:
                startTimer();
                break;
            case R.id.stopTimerBtn2:
                stopTimer();
                break;
            case R.id.resetTimerBtn2:
                resetTimer();
                break;
            default:
                break;
        }
    }

    private void resetTimer() {
        stopTimer();
        timeLeft.setText("");
        currTimeLftInt = 0;
    }

    private void stopTimer() {
        timer.cancel();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            timeLeft.setText(msg.arg1 + "");
            startTimer();
        }
    };

    private void startTimer() {
        Log.v(TAG, "倒计时时间："+timeLeft.getText().toString());
        if(timeLeft.getText().toString().equals("")){
            Toast.makeText(this,"请先设置倒计时时间",Toast.LENGTH_LONG).show();
            return;
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                currTimeLftInt--;
                Message message = mHandler.obtainMessage();
                message.arg1 = currTimeLftInt;
                mHandler.sendMessage(message);
            }
        };
        if(currTimeLftInt>0){
            timer.schedule(task,1000);
        }else{
            stopTimer();
            Toast.makeText(this,"时间到！",Toast.LENGTH_LONG).show();
        }
    }
}