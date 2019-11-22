package com.android.common.event

/**
 * @作者 陈忠岳
 * @主要功能 用来注解方法的类Subscribe。这个模仿了EventBus中的@Subscribe注解，该注解作用在方法上，默认值为MAIN
 * @创建日期  2019-11-21
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Subscribe(val threadMode: ThreadMode = ThreadMode.MAIN)