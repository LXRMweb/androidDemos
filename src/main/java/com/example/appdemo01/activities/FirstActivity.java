package com.example.appdemo01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdemo01.R;
import com.example.appdemo01.webview.WebviewUtils;

import cn.lxrm.demo.contactors.ConstractorsActivity;
import cn.lxrm.demo.crazycat.CatchCrazzyCatActivity;
import cn.lxrm.demo.fragment.TestFragmentActivity;
import cn.lxrm.demo.game2048.MainGame2048Activity;
import cn.lxrm.demo.mymenu.TestMyMenuActivity;
import cn.lxrm.demo.threads.TestThreadsMainActivity;
import cn.lxrm.demo.timer.Timer2Activity;
import cn.lxrm.demo.timer.TimerActivity;
import cn.lxrm.demo.view.TestViewMainActivity;
import cn.lxrm.demo.view.customview.TestDrawSinViewMainActivity;
import cn.lxrm.demo.view.lifecycle.TestViewLifecycleMainActivity;
import cn.lxrm.demo.view.surfaceview.TestSurfaceViewMainActivity;

public class FirstActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.appdemo01.MESSAGE";
    private final String TAG = this.getClass().getSimpleName();
    private EditText editTextMsg = null;
    private EditText editTextURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        editTextMsg = (EditText) findViewById(R.id.editTextTextPersonName);
        editTextURL = (EditText) findViewById(R.id.webviewPageURL);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = editTextMsg.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void showWebviewInAPP(View view){
        String message = editTextMsg.getText().toString();
        String url = "";
        switch (message){
            case "0":
                url = editTextURL.getText().toString();
                WebviewUtils.showWebviewWithLayoutXML(this, url);
                break;
            case "0-1":
                url = editTextURL.getText().toString();
                WebviewUtils.showWebviewWithLayoutXMLV1(this, url);
                break;
            case "1":
                url = editTextURL.getText().toString();
                WebviewUtils.showWebviewPageWithJava(this, url);
                break;
            case "2":
                String unencodedHtml =
                        "<head>\n" +
                                "    <meta charset=\"UTF-8\">\n" +
                                "    <title>Title</title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "  <h2>Welcome to Hybrid Dev World</h2>\n" +
                                "</body>\n" +
                                "</html>";
                WebviewUtils.showWebviewDataWithJava(this, unencodedHtml);
                break;
            case "3":
                String[] args = {"arg1", "arg2"};
                WebviewUtils.callJSMethod(this, "nativeCallJs", args);
                break;
            case "4":
                Intent intent = new Intent(FirstActivity.this, TimerActivity.class);
                startActivity(intent);
                break;
            case "5":
                Intent intent2 = new Intent(FirstActivity.this, Timer2Activity.class);
                startActivity(intent2);
                break;
            case "6":
                Intent intent3 = new Intent(FirstActivity.this, ConstractorsActivity.class);
                startActivity(intent3);
                break;
            case "7":
                Intent intent4 = new Intent(FirstActivity.this, CatchCrazzyCatActivity.class);
                startActivity(intent4);
                break;
            case "8":
                Intent intent5 = new Intent(FirstActivity.this, TestMyMenuActivity.class);
                startActivity(intent5);
                break;
            case "9":
                Intent intent6 = new Intent(FirstActivity.this, MainGame2048Activity.class);
                startActivity(intent6);
                break;
            case "10":
                Intent intent7 = new Intent(FirstActivity.this, TestFragmentActivity.class);
                startActivity(intent7);
                break;
            case "11":
                Intent intent8 = new Intent(FirstActivity.this, TestViewMainActivity.class);
                startActivity(intent8);
                break;
            case "12":
                Intent intent9 = new Intent(FirstActivity.this, TestViewLifecycleMainActivity.class);
                startActivity(intent9);
                break;
            case "13":
                Intent intent10 = new Intent(FirstActivity.this, TestDrawSinViewMainActivity.class);
                startActivity(intent10);
                break;
            case "14":
                Intent intent11 = new Intent(FirstActivity.this, TestThreadsMainActivity.class);
                startActivity(intent11);
                break;
            case "15":
                Intent intent12 = new Intent(FirstActivity.this, TestSurfaceViewMainActivity.class);
                startActivity(intent12);
                break;
            default:
                Toast.makeText(this,"?????????1-15?????????",Toast.LENGTH_LONG).show();
                break;
        }
    }
}