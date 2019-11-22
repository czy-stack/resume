package com.android.common.event

/**
 * @作者 陈忠岳
 * @主要功能 定义一个线程处理枚举类ThreadMode。这个模仿了EventBus中的@Subscribe注解中的threadMode，用来设置订阅者所在的线程
 * Schedulers.io( )：
用于IO密集型的操作，例如读写SD卡文件，查询数据库，访问网络等，具有线程缓存机制，在此调度器接收到任务后，先检查线程缓存池中，是否有空闲的线程，如果有，则复用，如果没有则创建新的线程，并加入到线程池中，如果每次都没有空闲线程使用，可以无上限的创建新线程。
Schedulers.newThread( )：
在每执行一个任务时创建一个新的线程，不具有线程缓存机制，因为创建一个新的线程比复用一个线程更耗时耗力，虽然使用Schedulers.io( )的地方，都可以使用Schedulers.newThread( )，但是，Schedulers.newThread( )的效率没有Schedulers.io( )高。
Schedulers.computation()：
用于CPU 密集型计算任务，即不会被 I/O 等操作限制性能的耗时操作，例如xml,json文件的解析，Bitmap图片的压缩取样等，具有固定的线程池，大小为CPU的核数。不可以用于I/O操作，因为I/O操作的等待时间会浪费CPU。
Schedulers.trampoline()：
在当前线程立即执行任务，如果当前线程有任务在执行，则会将其暂停，等插入进来的任务执行完之后，再将未完成的任务接着执行。
Schedulers.single()：
拥有一个线程单例，所有的任务都在这一个线程中执行，当此线程中有任务执行时，其他任务将会按照先进先出的顺序依次执行。
Scheduler.from(@NonNull Executor executor)：
指定一个线程调度器，由此调度器来控制任务的执行策略。
AndroidSchedulers.mainThread()：
在Android UI线程中执行任务，为Android开发定制。
 * @创建日期  2019-11-21
 */
enum class ThreadMode {
    SINGLE, COMPUTATION, IO, TRAMPOLINE, NEW_THREAD, MAIN
}