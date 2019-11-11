package me.ithome.flutter_geetest_plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.tencent.smtt.sdk.QbSdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterGeetestPlugin */
public class FlutterGeetestPlugin implements MethodCallHandler {
  //  private static final String captchaURL = "";
  private static String captchaURL = "https://www.geetest.com/demo/gt/register-slide";
  private static String validateURL = "https://www.geetest.com/demo/gt/validate-slide";
  private GT3GeetestUtils gt3GeetestUtils;
  private GT3ConfigBean gt3ConfigBean;
  //  private final Registrar mRegistrar;
  private static final String TAG = "flutter_geetest";
  private Result flutterResult;

  private FlutterGeetestPlugin(Registrar registrar) {
    //    this.mRegistrar = registrar;

    QbSdk.PreInitCallback cb =
        new QbSdk.PreInitCallback() {

          @Override
          public void onViewInitFinished(boolean arg0) {
            // x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            Log.e(TAG, " onViewInitFinished is " + arg0);
          }

          @Override
          public void onCoreInitFinished() {}
        };
    QbSdk.initX5Environment(registrar.context(), cb);
    gt3GeetestUtils = new GT3GeetestUtils(registrar.activity());

    // 配置bean文件，也可在oncreate初始化
    gt3ConfigBean = new GT3ConfigBean();
    // 设置验证模式，1：bind，2：unbind
    gt3ConfigBean.setPattern(1);
    // 设置点击灰色区域是否消失，默认不消息
    gt3ConfigBean.setCanceledOnTouchOutside(false);
    // 设置语言，如果为null则使用系统默认语言
    gt3ConfigBean.setLang(null);
    // 设置加载webview超时时间，单位毫秒，默认10000，仅且webview加载静态文件超时，不包括之前的http请求
    gt3ConfigBean.setTimeout(10000);
    // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
    gt3ConfigBean.setWebviewTimeout(10000);
    // 设置回调监听
    gt3ConfigBean.setListener(
        new GT3Listener() {
          /**
           * api1结果回调
           *
           * @param result
           */
          @Override
          public void onApi1Result(String result) {
            Log.e(TAG, "GT3BaseListener-->onApi1Result-->" + result);
          }

          /**
           * 验证码加载完成
           *
           * @param duration 加载时间和版本等信息，为json格式
           */
          @Override
          public void onDialogReady(String duration) {
            Log.e(TAG, "GT3BaseListener-->onDialogReady-->" + duration);
          }

          /**
           * 验证结果
           *
           * @param result
           */
          @Override
          public void onDialogResult(String result) {
            Log.e(TAG, "GT3BaseListener-->onDialogResult-->" + result);
            flutterResult.success(result);
            // 开启api2逻辑
            new RequestAPI2().execute(result);
          }

          /**
           * api2回调
           *
           * @param result
           */
          @Override
          public void onApi2Result(String result) {
            Log.e(TAG, "GT3BaseListener-->onApi2Result-->" + result);
          }

          @Override
          public void onStatistics(String s) {
            Log.e(TAG, "GT3BaseListener-->onStatistics-->" + s);
          }

          @Override
          public void onClosed(int i) {}

          @Override
          public void onSuccess(String s) {}

          @Override
          public void onFailed(GT3ErrorBean gt3ErrorBean) {}

          @Override
          public void onButtonClick() {
            new RequestAPI1().execute();
          }
        });
    gt3GeetestUtils.init(gt3ConfigBean);
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel =
        new MethodChannel(registrar.messenger(), "flutter_geetest_plugin");
    channel.setMethodCallHandler(new FlutterGeetestPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, @NotNull final Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "getGeetest":
        flutterResult = result;
        captchaURL = call.argument("api1");
        validateURL = call.argument("api2");
        // 开启验证
        gt3GeetestUtils.startCustomFlow();
        //        gt3GeetestUtils.getGeetest(
        //            mRegistrar.activeContext(),
        //            url,
        //            validateURL,
        //            null,
        //            new GT3GeetestBindListener() {
        //
        //              @Override
        //              public boolean gt3SetIsCustom() {
        //                return false;
        //              }
        //
        //              @Override
        //              public void gt3GetDialogResult(String json) {
        //                super.gt3GetDialogResult(json);
        //                try {
        //                  JSONObject jsonObject = new JSONObject(json);
        //                  String gt3Challenge = jsonObject.getString("geetest_challenge");
        //                  String gt3Validate = jsonObject.getString("geetest_validate");
        //                  String gt3Seccode = jsonObject.getString("geetest_seccode");
        //                  result.success(json);
        //                  Log.i("gt3", "gt3GetDialogResult-->" + gt3Challenge + " " +
        // gt3Validate);
        //                  gt3GeetestUtils.gt3TestFinish();
        //                } catch (JSONException e) {
        //                  gt3GeetestUtils.gt3TestClose();
        //                  e.printStackTrace();
        //                }
        //              }
        //
        //              @Override
        //              public void gt3DialogOnError(String s) {
        //                super.gt3DialogOnError(s);
        //              }
        //            });
        //        gt3GeetestUtils.setDialogTouch(false);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  /** 请求api1 */
  @SuppressLint("StaticFieldLeak")
  class RequestAPI1 extends AsyncTask<Void, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(Void... params) {
      String string = HttpUtils.requestGet(captchaURL);
      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject(string);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject parmas) {
      // 继续验证
      Log.i(TAG, "RequestAPI1-->onPostExecute: " + parmas);
      // SDK可识别格式为
      // {"success":1,"challenge":"06fbb267def3c3c9530d62aa2d56d018","gt":"019924a82c70bb123aae90d483087f94"}
      // 设置返回api1数据
      // TODO 将api1请求结果传入此方法，即使为null也要传入，SDK内部已处理
      gt3ConfigBean.setApi1Json(parmas);
      gt3GeetestUtils.getGeetest();
    }
  }

  /** 请求api2 */
  @SuppressLint("StaticFieldLeak")
  class RequestAPI2 extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
      if (!TextUtils.isEmpty(params[0])) {
        return HttpUtils.requestPost(validateURL, params[0]);
      } else {
        return null;
      }
    }

    @Override
    protected void onPostExecute(String result) {
      Log.i(TAG, "RequestAPI2-->onPostExecute: " + result);
      if (!TextUtils.isEmpty(result)) {
        try {
          JSONObject jsonObject = new JSONObject(result);
          String status = jsonObject.getString("status");

          if ("success".equals(status)) {
            gt3GeetestUtils.showSuccessDialog();
          } else {
            gt3GeetestUtils.showFailedDialog();
          }
        } catch (Exception e) {
          e.printStackTrace();
          gt3GeetestUtils.showFailedDialog();
        }
      } else {
        gt3GeetestUtils.showFailedDialog();
      }
    }
  }
}
