package cn.studyjams.s1.sj46.tablayoutdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-07-18.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private ImageButton registerButton;
    private ImageButton sendCodeButton;
    private EditText registerPhone;
    private EditText registerCode;
    private EditText registerPwd;

    private String url;
    private String phone;
    private String password;
    private String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View loginView = inflater.inflate(R.layout.register_fragment, container, false);
        return loginView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerButton = (ImageButton) getActivity().findViewById(R.id.register_button);
        sendCodeButton = (ImageButton) getActivity().findViewById(R.id.send_code);
        registerPhone = (EditText) getActivity().findViewById(R.id.register_phone);
        registerPwd = (EditText) getActivity().findViewById(R.id.register_pwd);
        registerCode = (EditText) getActivity().findViewById(R.id.code);

        sendCodeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        phone = registerPhone.getText().toString();
        password = registerPwd.getText().toString();
        code = registerCode.getText().toString();
        int id = view.getId();
        switch (id)
        {
            case R.id.send_code:
                Log.d("Register","click send code");
                if (phone != null && phone.length()>0)
                {
                    Log.d("Register","Y");
                    sendCode(phone);
                }else{
                    Log.d("Register","N");
                    Toast.makeText(getActivity(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_button:
                Log.d("Register","click register button");
                register(phone,password,code);
                break;
        }
    }

    private void register(final String phone,final String pwd,final String code) {
        Log.d("Register","begin register");
        Map<String, String> params = new HashMap<>();
        params.put("username",phone);
        params.put("password",pwd);
        params.put("userSmsCodes",code);
        url = InterfaceTool.getBaseUrlPrefix(getActivity()) + getString(R.string.interface_register);
        Log.d("Register",url);
        HttpUtil.postAsyn(url, new HttpUtil.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response, String responseString) {
                Log.d("Register",responseString);
            }
        },params);
    }

    private void sendCode(final String phone) {
        Log.d("Register","begin send code");
        Map<String, String> params = new HashMap<>();
        params.put("sidphone",phone);
        params.put("smsType",1+"");
        params.put("userType",1+"");
        url = InterfaceTool.getBaseUrlPrefix(getActivity()) + getString(R.string.interface_phonecode);
        Log.d("Register",url);
        HttpUtil.postAsyn(url, new HttpUtil.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                
            }

            @Override
            public void onResponse(Response response, String responseString) {
                Log.d("Register",responseString);
            }
        },params);
    }
}
