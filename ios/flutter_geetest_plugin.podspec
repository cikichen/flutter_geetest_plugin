#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_geetest_plugin'
  s.version          = '0.2.2'
  s.summary          = 'Geetest(GT3Captcha) Flutter plugin(Geetest&#x27;s CAPTCHA - a user-friendly CAPTCHA powered by AI. Protect your website and mobile apps against bot traffic. Start your free trial now!).'
  s.description      = <<-DESC
Geetest(GT3Captcha) Flutter plugin(Geetest&#x27;s CAPTCHA - a user-friendly CAPTCHA powered by AI. Protect your website and mobile apps against bot traffic. Start your free trial now!).
                       DESC
  s.homepage         = 'https://github.com/cikichen/flutter_geetest_plugin'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'admin@ithome.me' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'GT3Captcha-iOS'

  s.ios.deployment_target = '8.0'
end

