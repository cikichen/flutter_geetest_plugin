[![Build Status](https://travis-ci.com/cikichen/flutter_geetest_plugin.svg?branch=master)](https://travis-ci.com/cikichen/flutter_geetest_plugin)

# geetest_plugin

Geetest Flutter plugin.

## Getting Started

Basic function version, waiting for iteration.

```
  Future<void> getGeetest() async {
    String result;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      result = await GeetestPlugin.getGeetest('url: api1');
    } on PlatformException {
//      _json = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _json = result;
    });
  }
 ```