package cn.studyjams.s1.sj46.tablayoutdemo;

import android.content.Context;

/**
 * Created by Administrator on 2016-07-21.
 */
public class InterfaceTool {
    private static final String PROTOCOL = "http://";
//    private Context context;

    public static String getBaseUrlPrefix(Context context) {
        StringBuffer sb = new StringBuffer(PROTOCOL);
        sb.append(context.getString(R.string.interface_ip));
        if (context.getString(R.string.interface_port) != null && context.getString(R.string.interface_port).length() > 0)
            sb.append(":").append(context.getString(R.string.interface_port));
        String projectName = context.getString(R.string.interface_pro_name);
        if (projectName != null && projectName.trim().length() > 0) {
            sb.append("/").append(projectName);
        }
        return sb.toString();
    }



//    public static void getPhoneCodeAction(Context context, int id, HttpDataCallback<User> callback) {
//        HttpUtil.Param param = new HttpUtil.Param("id", id + "");
//        HttpUtil.postAsyn(InterfaceTool.getBaseUrlPrefix(context) + context.getString(R.string.interface_phonecode), callback, param);
//    }
//
//    public static void getRegister(Context context,int)
}
