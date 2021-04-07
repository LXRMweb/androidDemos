package cn.lxrm.demo.game2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdemo01.R;

public class MainGame2048Activity extends AppCompatActivity implements View.OnClickListener {
    private int score;
    private TextView tvScore;

    // --------- start 获取到自己的实例的引用 -----------

    private static MainGame2048Activity mainGame2048Activity = null;

    public MainGame2048Activity() {
        // 将自己的实例的引用保存下来
        mainGame2048Activity = this;
    }

    /** Description: 获取到自己的实例的引用
     * @author created by ChenMeiYu at 2021-4-7 16:26, v1.0
     */
    public static MainGame2048Activity getMainGame2048Activity() {
        return mainGame2048Activity;
    }
    // --------- end 获取到自己的实例的引用 -----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game2048);
        // 设置屏幕方向：水平方向（不随设备转动而改变方向）
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tvScore = (TextView) findViewById(R.id.tv_score_game2048);
    }

    public void clearScore(){
        this.score = 0;
        showScore();
    }

    public void setScore(int score) {
        this.score = score;
        showScore();
    }

    private void showScore() {
        tvScore.setText("" + this.score);
    }

    /** Description: 积分增加
     * @author created by ChenMeiYu at 2021-4-7 16:30, v1.0
     * @param s:int 新增积分
     * @return void
     */
    public void addScore(int s){
        this.score+=s;
        showScore();
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRestart:
                Toast.makeText(this,"todo-重启",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}