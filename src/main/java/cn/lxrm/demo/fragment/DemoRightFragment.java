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

    /** Description: activity???fragment????????? | fragment?????????activity????????????
     * @author created by ChenMeiYu at 2021-4-13 8:57, v1.0
     */
    public void callActivityFuncInFragment() {
        Toast.makeText(getContext(),"???????????????" + this.TAG + "#callActivityFuncInFragment()",Toast.LENGTH_LONG).show();
        // ???????????????????????????????????????getActivity()??????????????????fragment????????????activity????????????
        TestFragmentActivity testFragmentActivity = (TestFragmentActivity) getActivity();
        // ????????????activity???????????????????????????activity????????????
        testFragmentActivity.funcDefinedInActivity();
    }

    /** Description: activity???fragment??????????????? | ?????????fragment??????????????????fragment????????????func
     * @author created by ChenMeiYu at 2021-4-13 9:14, v1.0
     */
    public void callLeftFragmentFuncInRightFragment() {
        Toast.makeText(getContext(),"????????????"+this.TAG+"#callLeftFragmentFuncInRightFragment",Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(),"????????????DemoRightFragment#callLeftFragmentFuncInRightFragment",Toast.LENGTH_LONG).show();
        // step1, ???????????????fragment??????activity
        TestFragmentActivity testFragmentActivity = (TestFragmentActivity) getActivity();
        // step2, ??????activity?????????????????????fragment???????????????
        DemoLeftFragment leftFragment = (DemoLeftFragment) testFragmentActivity.getFragmentManager().findFragmentById(R.id.left_fragment);
        // step3, ????????????fragment??????????????????func
        leftFragment.funcDefinedInLeftFragment();
    }
}