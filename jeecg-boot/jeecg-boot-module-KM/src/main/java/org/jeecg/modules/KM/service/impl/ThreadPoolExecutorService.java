package org.jeecg.modules.KM.service.impl;

import org.jeecg.modules.KM.service.IThreadPoolExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadPoolExecutorService implements IThreadPoolExecutorService {

    private static LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(10000);
    private static ThreadPoolExecutor singleExecutor =new ThreadPoolExecutor(
            1,10000, 20, TimeUnit.SECONDS,deque);
    private static ArrayBlockingQueue<Runnable> poolQueue = new ArrayBlockingQueue<>(10);
    private static ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(
            5,20, 5, TimeUnit.SECONDS,poolQueue);

    private Logger logger= LoggerFactory.getLogger(ThreadPoolExecutorService.class);
    public ThreadPoolExecutorService(){
        poolExecutor.allowCoreThreadTimeOut(true);
        singleExecutor.allowCoreThreadTimeOut(true);
    }

    @Override
    public void singleExecute(Runnable runnable){
        singleExecutor.execute(() -> {
            runnable.run();
        });
    }

    @Override
    public void execute(Runnable runnable){
        poolExecutor.execute(() -> {
            runnable.run();
        });
    }

    @PreDestroy
    public void destroy(){
        try {
            poolExecutor.getQueue().clear();
            poolExecutor.shutdown();
            poolExecutor.awaitTermination(10,TimeUnit.SECONDS);
            singleExecutor.getQueue().clear();
            singleExecutor.shutdown();
            singleExecutor.awaitTermination(10,TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("销毁线程池",e);
        }
        logger.info("销毁线程池");
    }

}
