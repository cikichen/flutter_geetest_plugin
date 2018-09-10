import 'dart:async';

import 'package:flutter/services.dart';

class GeetestPlugin {
  static const MethodChannel _channel =
  const MethodChannel('geetest_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> getGeetest(String url) async {
    final Map<String, dynamic> params = <String, dynamic>{
      'key': url,
    };
    final String result = await _channel.invokeMethod('getGeetest', params);
    return result;
  }
}
