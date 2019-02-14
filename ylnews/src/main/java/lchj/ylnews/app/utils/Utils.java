package lchj.ylnews.app.utils;

import android.content.Context;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import lchj.ylnews.R;
import lchj.ylnews.app.BaseApplication;
import lchj.ylnews.mvp.model.entity.UserBean;
import me.jessyan.art.base.App;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.utils.PermissionUtil;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import retrofit2.HttpException;

public class Utils {
    public static void setUser(UserBean user) {
        ((App) BaseApplication.mContext).getAppComponent().extras().put("user", user);
    }
    public static UserBean getUser() {
        return (UserBean) ((App) BaseApplication.mContext).getAppComponent().extras().get("user");
    }

    public static String getToken(Context context) {
        return (String) ((App) context.getApplicationContext()).getAppComponent().extras().get("token");
    }

    public static void setToken(Context context, String token) {
        ((App) context.getApplicationContext()).getAppComponent().extras().put("token", token);
    }
    //获取网络请求权限
    public static void getPermissions(IView view, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }
            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                view.showMessage(BaseApplication.mContext.getString(R.string.get_permission_fail));
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                view.showMessage(BaseApplication.mContext.getString(R.string.goto_permission_setting));
            }
        }, rxPermissions, errorHandler);
    }

    public static String getThrowsMsg(Throwable t) {
        String msg = "操作失败";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof ConnectException) {
            msg = "连接服务器失败";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }

        return msg;
    }
    private static String convertStatusCode(HttpException httpException) {
        String msg = "";
        int code = httpException.code();
        if (code == 500) {
            msg = "服务器发生错误";
        } else if (code == 404) {
            msg = "请求地址不存在";
        } else if (code == 403) {
            msg = "请求被服务器拒绝";
        } else if (code == 307) {
            msg = "请求被重定向到其他页面";
        } else if (code == 401) {
            msg = "请求无权限";
            //当前请求没有权限，token过期，退出到登录界面
//            EventBus.getDefault().post(new PlanListOrLoginEvent(Api.RequestTokenOver, LoginActivity.class));
        } else {
//            msg = TextUtils.isEmpty(httpException.message()) ?
//                    "请求失败[" + code + "]" : httpException.message();
            msg = "请求失败";
        }

        return msg;
    }
}
