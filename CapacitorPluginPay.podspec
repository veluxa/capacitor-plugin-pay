require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name = 'CapacitorPluginPay'
  s.version = package['version']
  s.summary = package['description']
  s.license = package['license']
  s.homepage = package['repository']['url']
  s.author = package['author']
  s.source = { :git => package['repository']['url'], :tag => s.version.to_s }
  s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
  s.ios.deployment_target  = '11.0'
  s.dependency 'Capacitor'
  s.swift_version = '5.1'
  s.vendored_frameworks = 'ios/AlipaySDK.framework'
  s.vendored_libraries  = 'ios/Plugin/WXSDK/libWeChatSDK.a'
  s.xcconfig = { 'OTHER_LDFLAGS' => '-ObjC -all_load' }
  s.frameworks = 'SystemConfiguration', 'Security', 'CoreTelephony', 'CFNetwork', 'UIKit', 'QuartzCore', 'CoreText', 'CoreGraphics', 'Foundation', 'CoreMotion'
  s.libraries = 'z', 'c++'

  s.public_header_files = 'ios/Plugin/AlipaySDK/*.h', 'ios/Plugin/WXSDK/*.h'
  s.resource_bundles = {
    'AlipaySDK' => ['ios/Plugin/AlipaySDK/AlipaySDK.bundle']
  }

end
