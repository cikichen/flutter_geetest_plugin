package me.ithome.geetestplugin;

import android.util.Log;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;

import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * GeetestPlugin
 */
public class GeetestPlugin implements MethodCallHandler {

    private static final String captchaURL = "";
    //private static final String captchaURL = "http://www.geetest.com/demo/gt/register-slide";
    private static final String validateURL = "http://www.geetest.com/demo/gt/validate-slide";
    private static GT3GeetestUtilsBind gt3GeetestUtils;
    private final Registrar mRegistrar;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "geetest_plugin");
        channel.setMethodCallHandler(new GeetestPlugin(registrar));
        gt3GeetestUtils = new GT3GeetestUtilsBind(registrar.activity());
        // 设置是否可以点击Dialog灰色区域关闭验证码
        gt3GeetestUtils.setDialogTouch(true);
        // 设置debug模式，开代理抓包可使用，默认关闭，生产环境务必设置为false
        gt3GeetestUtils.setDebug(true);
        // 设置加载webview超时时间，单位毫秒，默认15000，仅且webview加载静态文件超时，不包括之前的http请求
        gt3GeetestUtils.setTimeout(15000);
        // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
        gt3GeetestUtils.setWebviewTimeout(10000);
    }

    private GeetestPlugin(Registrar registrar) {
        this.mRegistrar = registrar;
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "getGeetest":
                String url = call.argument("key");
                gt3GeetestUtils.getGeetest(mRegistrar.activeContext(), url, validateURL, null,
                        new GT3GeetestBindListener() {

                            @Override
                            public boolean gt3SetIsCustom() {
                                return false;
                            }

                            @Override
                            public void gt3GetDialogResult(String json) {
                                super.gt3GetDialogResult(json);
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    String gt3Challenge = jsonObject.getString("geetest_challenge");
                                    String gt3Validate = jsonObject.getString("geetest_validate");
                                    String gt3Seccode = jsonObject.getString("geetest_seccode");
                                    result.success(json);
                                    Log.i("gt3", "gt3GetDialogResult-->" + gt3Challenge + " " + gt3Validate);
                                    gt3GeetestUtils.gt3TestFinish();
                                } catch (JSONException e) {
                                    gt3GeetestUtils.gt3TestClose();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void gt3DialogOnError(String s) {
                                super.gt3DialogOnError(s);
                            }
                        });
                gt3GeetestUtils.setDialogTouch(false);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
