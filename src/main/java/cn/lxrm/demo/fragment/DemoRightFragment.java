package cn.lxrm.demo.fragment;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appdemo01.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoRightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoRightFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DemoRightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemoFragmentRight.
     */
    // TODO: Rename and change types and number of parameters
    public static DemoRightFragment newInstance(String param1, String param2) {
        DemoRightFragment fragment = new DemoRightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_demo_right, container, false);
    }

    /** Description: activity和fragment的通信 | fragment中调用activity中的方法
     * @author created by ChenMeiYu at 2021-4-13 8:57, v1.0
     */
    public void callActivityFuncInFragment() {
        Toast.makeText(getContext(),"您已经进入" + this.TAG + "#callActivityFuncInFragment()",Toast.LENGTH_LONG).show();
        // 在每个碎片中都可以通过调用getActivity()来得到和当前fragment相关联的activity对象引用
        TestFragmentActivity testFragmentActivity = (TestFragmentActivity) getActivity();
        // 使用上述activity对象调用定义在上述activity中的方法
        testFragmentActivity.funcDefinedInActivity();
    }

    /** Description: activity与fragment之间的通信 | 在一个fragment中调用另一个fragment中定义的func
     * @author created by ChenMeiYu at 2021-4-13 9:14, v1.0
     */
    public void callLeftFragmentFuncInRightFragment() {
        Toast.makeText(getContext(),"您已进入"+this.TAG+"#callLeftFragmentFuncInRightFragment",Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(),"您已进入DemoRightFragment#callLeftFragmentFuncInRightFragment",Toast.LENGTH_LONG).show();
        // step1, 获取到当前fragment所属activity
        TestFragmentActivity testFragmentActivity = (TestFragmentActivity) getActivity();
        // step2, 通过activity对象获取到其他fragment对象的引用
        DemoLeftFragment leftFragment = (DemoLeftFragment) testFragmentActivity.getFragmentManager().findFragmentById(R.id.left_fragment);
        // step3, 使用其他fragment对象中定义的func
        leftFragment.funcDefinedInLeftFragment();
    }
}