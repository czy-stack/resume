package com.android.common.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
object RxUtils {
    //    val compositeDisposable = CompositeDisposable()
    //延迟
    fun timer(delay: Long, unit: TimeUnit, consumer: Consumer<Long>): Disposable {
        return Observable.timer(delay, unit).subscribe(consumer)
    }
    fun timerUI(delay: Long, unit: TimeUnit, consumer: Consumer<Long>): Disposable {
        return Observable.timer(delay, unit).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer)
    }

    fun timeFalse(consumer: Consumer<Long>): Disposable{
        return Observable.timer(1200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer)
    }

    //循环
    fun interval(delay: Long, interval: Long, unit: TimeUnit, consumer: Consumer<Long>): Disposable {
        return Observable.interval(delay, interval, unit).subscribe(consumer)
    }

    fun intervalUI(delay: Long, interval: Long, unit: TimeUnit, consumer: Consumer<Long>): Disposable {
        return Observable.interval(delay, interval, unit).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer)
    }
    /*
    * debounce：防抖；
only emit an item from an Observable if a particular time-span has passed without it emitting another item,
当一个事件发送出来之后，在约定时间内没有再次发送这个事件，则发射这个事件，如果再次触发了，则重新计算时间。
    * */
//    fun debounce(delay: Long,  unit: TimeUnit,consumer: Consumer<Long>): Disposable{
//        return Observable.create(object:ObservableOnSubscribe<Long>{
//            override fun subscribe(emitter: ObservableEmitter<Long>) {
//                LogUtils.i("debounce")
//                emitter.onNext(emitter.)
//            }
//        }).debounce(delay,unit)
//            .subscribe(consumer)
//    }

//    fun debounceUI(delay: Long,  unit: TimeUnit,consumer: Consumer<Long>): Disposable{
//        return Observable.create(ObservableOnSubscribe<Long> { }).debounce(delay,unit)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(consumer)
//    }
}