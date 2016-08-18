package cn.studyjams.s1.sj46.tablayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016-07-18.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private ImageButton loginButton;
    private EditText inputPhone;
    private EditText inputPassword;
    private String url;
    private String phone;
    private String password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View loginView = inflater.inflate(R.layout.login_fragment, container, false);
        return loginView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("LoginFragment","onActivityCreate");
        loginButton = (ImageButton) getActivity().findViewById(R.id.login_button);
        inputPhone = (EditText) getActivity().findViewById(R.id.account);
        inputPassword = (EditText) getActivity().findViewById(R.id.password);
        loginButton.setOnClickListener(this);
    }

    private void loginByPhone(final String phone, final String pwd) {
        Log.d("LoginFragment", "begin");
        Map<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", pwd);
        params.put("state", 1 + "");
        url = InterfaceTool.getBaseUrlPrefix(getActivity()) + getString(R.string.interface_login);
        Log.d("LoginFragment", url);
        HttpUtil.postAsyn(url, new HttpUtil.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response, String responseString) {
                Log.d("LoginFragment", responseString);
                Result mResult = GsonUtil.normalformate(responseString, Result.class);
                Log.d("LoginFragment", String.valueOf(mResult.isSuccess()));
                if (mResult.isSuccess()) {
                    Log.d("LoginFragment", "准备跳转");
                    User user = GsonUtil.normalformate(mResult.getData().toString(),User.class);
                    //传递解析出来的user对象 user类要实现接口Serializable
                    Intent intent = new Intent(getActivity(), UserActivity.class);
//                    intent.putExtra("username",user.getRealname());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Log.d("LoginFragment", "错误");
                    Toast.makeText(getActivity(), mResult.getMsg(), Toast.LENGTH_SHORT).show();
                    inputPassword.setText("");
                }
            }
        }, params);
    }

    @Override
    public void onClick(View view) {
        phone = inputPhone.getText().toString();
        password = inputPassword.getText().toString();
        Log.d("LoginFragment", phone + password);
        loginByPhone(phone, password);

//        CircularAnimUtil.startActivity(getActivity(),UserActivity.class,view,R.color.colorBase);
    }
}
