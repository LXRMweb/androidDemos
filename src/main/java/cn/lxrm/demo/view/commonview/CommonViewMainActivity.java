package cn.lxrm.demo.view.commonview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.appdemo01.R;

public class CommonViewMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_common_view);
        EditText editText = (EditText) findViewById(R.id.editTextDemo01);
    }
}