import 'dart:async';

import 'package:flutter/services.dart';

class FlutterGeetestPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_geetest_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  ///一键验证
  static Future<String> getGeetest(String api1, String api2) async {
    final Map<String, dynamic> params = <String, dynamic>{
      'api1': api1,
      'api2': api2
    };
    final String result = await _channel.invokeMethod('getGeetest', params);
    return result;
  }
}
