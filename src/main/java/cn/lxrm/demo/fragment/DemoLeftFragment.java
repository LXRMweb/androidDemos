package cn.lxrm.demo.fragment;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appdemo01.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoLeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoLeftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = this.getClass().getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DemoLeftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemoLeftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemoLeftFragment newInstance(String param1, String param2) {
        DemoLeftFragment fragment = new DemoLeftFragment();
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
        return inflater.inflate(R.layout.fragment_demo_left, container, false);
    }

    /** Description: 用于测试fragment和activity的通信
     * @author created by ChenMeiYu at 2021-4-13 9:41, v1.0
     */
    public void funcDefinedInLeftFragment(){
        Log.v(TAG,"您已进入"+this.TAG+"#callLeftFragmentFuncInRightFragment");
        Toast.makeText(getContext(),"LeftFragment中定义的一个函数",Toast.LENGTH_SHORT).show();
    }
}