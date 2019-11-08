#import "FlutterGeetestPlugin.h"

//网站主部署的用于验证注册的接口 (api_1)
//#define api_1 @""
//网站主部署的二次验证的接口 (api_2)
//#define api_2 @"https://www.geetest.com/demo/gt/validate-slide"

@interface FlutterGeetestPlugin () <GT3CaptchaManagerDelegate, GT3CaptchaManagerViewDelegate>

@property (nonatomic, strong) UIActivityIndicatorView *indicatorView;

@property (nonatomic, strong) GT3CaptchaManager *manager;

@property (nonatomic, strong) NSString *originalTitle;

@property (nonatomic, assign) BOOL titleFlag;

@end

@implementation FlutterGeetestPlugin{
    FlutterResult _eventResult;
    NSString *api1;
    NSString *api2;
}
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_geetest_plugin"
            binaryMessenger:[registrar messenger]];
  FlutterGeetestPlugin* instance = [[FlutterGeetestPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"getGeetest" isEqualToString:call.method]) {
      NSDictionary *arguments = [call arguments];
      api1 = arguments[@"api1"];
      api2 = arguments[@"api2"];
      _eventResult = result;
      self.indicatorView = [self createActivityIndicator];
      [self.manager registerCaptcha:nil];
      [self.manager disableBackgroundUserInteraction:YES];
      [self.manager startGTCaptchaWithAnimated:YES];
  }else {
    result(FlutterMethodNotImplemented);
  }
}

- (UIActivityIndicatorView *)createActivityIndicator {
    UIActivityIndicatorView *indicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    [indicatorView setHidesWhenStopped:YES];
    [indicatorView stopAnimating];
    
    return indicatorView;
}

- (GT3CaptchaManager *)manager {
    if (!_manager) {
        _manager = [[GT3CaptchaManager alloc] initWithAPI1:api1 API2:api2 timeout:15.0];
        _manager.delegate = self;
        [_manager enableDebugMode:true];
//        [_manager useVisualViewWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleDark]];
    }
    return _manager;
}

- (void)gtCaptcha:(GT3CaptchaManager *)manager didReceiveSecondaryCaptchaData:(NSData *)data response:(NSURLResponse *)response error:(GT3Error *)error decisionHandler:(void (^)(GT3SecondaryCaptchaPolicy))decisionHandler {
    if (!error) {
        //处理你的验证结果
        NSString * _Nullable extractedExpr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
//        NSLog(@"\ndata: %@", extractedExpr);
//        NSLog(@"\n session ID: %@,\ndata: %@", [manager getCookieValue:@"msid"], [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
        
        NSDictionary * dataDic = [[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil]copy];
//        NSLog(@"\ndata: %@", dataDic);
        if (dataDic[@"error_code"]) {
            //失败请调用decisionHandler(GT3SecondaryCaptchaPolicyForbidden)
            decisionHandler(GT3SecondaryCaptchaPolicyForbidden);
        } else {
            //成功请调用decisionHandler(GT3SecondaryCaptchaPolicyAllow)
            decisionHandler(GT3SecondaryCaptchaPolicyAllow);
        }
        if (!_eventResult) return;
        _eventResult(extractedExpr);
        _eventResult = nil;
        extractedExpr = nil;
    }
    else {
        //二次验证发生错误
        decisionHandler(GT3SecondaryCaptchaPolicyForbidden);
//        [TipsView showTipOnKeyWindow:error.error_code fontSize:12.0];
        NSLog(@"validate error: %ld, %@", (long)error.code, error.localizedDescription);
    }
}

- (void)gtCaptcha:(GT3CaptchaManager *)manager didReceiveCaptchaCode:(NSString *)code result:(NSDictionary *)result message:(NSString *)message {
//    NSLog(@"\ndata: %@", result);
    if (!_eventResult) return;
    NSData *data = [NSJSONSerialization dataWithJSONObject:result options:NSJSONWritingPrettyPrinted error:nil];
    NSString *myResult = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
    _eventResult(myResult);
    _eventResult = nil;
}

- (BOOL)shouldUseDefaultSecondaryValidate:(GT3CaptchaManager *)manager {
    return NO;
}

- (void)gtCaptcha:(GT3CaptchaManager *)manager errorHandler:(GT3Error *)error {
    //处理验证中返回的错误
//    NSLog(@"error: %@", error.localizedDescription);
//     NSLog(@"error: %li", (long)error.code);
    if (error.code == -999) {
        // 请求被意外中断, 一般由用户进行取消操作导致, 可忽略错误
    }
    else if (error.code == -10) {
        // 预判断时被封禁, 不会再进行图形验证
    }
    else if (error.code == -20) {
        // 尝试过多
    }
    else {
        // 网络问题或解析失败, 更多错误码参考开发文档
       
    }
}

@end
