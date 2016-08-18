package cn.studyjams.s1.sj46.tablayoutdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-07-22.
 */
public class UserActivity extends Activity{

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Log.d("UserActivity","onCreate");
        welcomeText = (TextView) findViewById(R.id.welcome);
        Intent intent = getIntent();
//        String username = intent.getStringExtra("username");
        //获取传递来的对象
        User user = (User) intent.getSerializableExtra("user");
        welcomeText.setText(user.getUsername()+",welcome!"+user.getCreatetime());
    }
}
