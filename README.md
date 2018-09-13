[![pub package](https://img.shields.io/badge/pub-0.0.1-brightgreen.svg)](https://pub.dartlang.org/packages/geetest_plugin) [![Build Status](https://travis-ci.com/cikichen/flutter_geetest_plugin.svg?branch=master)](https://travis-ci.com/cikichen/flutter_geetest_plugin)

# geetest_plugin

Geetest Flutter plugin.

## Screenshot

![Screenshot](screenshots/demo.gif)

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

 ## FAQ: 

 1. [!] The 'Pods-Runner' target has transitive dependencies that include static binaries:

 commenting out `use_frameworks`