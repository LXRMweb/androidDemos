package cn.lxrm.demo.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appdemo01.R;

public class TestFragmentActivity extends Activity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();

    private Button btn1LftFragment;
    private Button btn2LftFragment;
    private Button btn3LftFragment;
    private Button btn4LftFragment;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);

        btn1LftFragment = (Button) findViewById(R.id.demo_fragment_left_btn1);
        btn1LftFragment.setOnClickListener(this);
        btn2LftFragment = (Button) findViewById(R.id.demo_fragment_left_btn2);
        btn2LftFragment.setOnClickListener(this);
        btn3LftFragment = (Button) findViewById(R.id.demo_fragment_left_btn3);
        btn3LftFragment.setOnClickListener(this);
        btn4LftFragment = (Button) findViewById(R.id.demo_fragment_left_btn4);
        btn4LftFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demo_fragment_left_btn1:
                Fragment fragment = null;
                // 运行时动态替换fragment
                fragment = (Fragment) (this.flag ? new DemoRightFragment() : new DemoAnotherRightFragment());
                flag = !flag;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.right_layout, fragment);
                /*addToBackStack: 返回栈
                 *   如果不调用addToBackStack，则按返回键，会直接退出
                 *   如果事务提交之前调用了addToBackStack，则按返回键会回到上一个fragment，而非直接退出
                 * */
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.demo_fragment_left_btn2:
                // activity和fragment通信：activity中调用fragment中的func
                // step1, 先通过activity中的getFragmentManager()获取到相应Fragment对象的引用
                DemoLeftFragment leftFragment = (DemoLeftFragment) getFragmentManager().findFragmentById(R.id.left_fragment);
                // step2, 使用上述步骤获取到的fragment对象的引用调用fragment中定义的func
                leftFragment.funcDefinedInLeftFragment();
                break;
            case R.id.demo_fragment_left_btn3:
                // fragment中调用activity中func
                DemoRightFragment rightFragment = (DemoRightFragment) getFragmentManager().findFragmentById(R.id.right_fragment);
                rightFragment.callActivityFuncInFragment();
                break;
            case R.id.demo_fragment_left_btn4:
                // fragment中调用另一个fragment中的func
                DemoRightFragment rightFragment1 = (DemoRightFragment) getFragmentManager().findFragmentById(R.id.right_fragment);
                rightFragment1.callLeftFragmentFuncInRightFragment();
                break;
            default:
                break;
        }

    }

    /** Description: 定义在activity中的func
     *      用于检验fragment中是否可调用到activity中的方法
     * @author created by ChenMeiYu at 2021-4-13 9:02, v1.0
     */
    public void funcDefinedInActivity() {
        Toast.makeText(this,"这是一个定义在" + this.TAG + "中的方法",Toast.LENGTH_SHORT).show();
    }
}