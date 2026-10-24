# 演示项目：保留最小但必要的混淆规则，确保 release 包可运行
#
# 核心风险点：LitePal 与 Gson 都依赖「字段名」反射，一旦被 R8 重命名就会失效。
# 实测未加规则时：ShareData.symbol -> g、name -> h，导致
#   1) LitePal 建表/查询的列名变化，硬编码 SQL 直接 no such column
#   2) Gson 按字段名匹配 JSON，反序列化结果全为 null / 0

# ---------- 数据模型：LitePal 表结构 + Gson 反序列化 ----------
-keep class com.example.kotlin.bean.** { *; }
-keep class com.example.java.bean.** { *; }

# ---------- LitePal ----------
-keep class org.litepal.** { *; }
-keep class * extends org.litepal.crud.LitePalSupport { *; }
-dontwarn org.litepal.**

# ---------- Gson ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# ---------- EventBus ----------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# ---------- 自研事件总线：反射查找 @Subscribe 方法与线程模式 ----------
-keep class com.android.common.event.** { *; }
-keepclassmembers class * {
    @com.android.common.event.Subscribe <methods>;
}
-keep class com.android.common.event.ThreadMode { *; }

# ---------- WebView JS 桥（当前未使用，保留以免后续接入踩坑） ----------
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ---------- 阿里云热修复 ----------
-keep class com.taobao.sophix.** { *; }
-keep class com.ta.utdid2.device.** { *; }
-dontwarn com.taobao.sophix.**
-dontwarn com.ta.utdid2.**

# ---------- 通用 ----------
# 保留行号，崩溃栈才可读
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
