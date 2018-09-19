#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_geetest_plugin'
  s.version          = '0.0.6'
  s.summary          = 'Geetest(GT3Captcha) Flutter plugin.'
  s.description      = <<-DESC
Geetest Flutter plugin.
                       DESC
  s.homepage         = 'https://github.com/cikichen/flutter_geetest_plugin'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'SimonChen' => 'admin@ithome.me' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'GT3Captcha'
  
  s.ios.deployment_target = '8.0'
end

