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
//    private ThreadPoolExecutor poolExecutor;

    private static LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<Runnable>(10000);
    private static ThreadPoolExecutor singleExecutor =new ThreadPoolExecutor(
            1,10000, 20, TimeUnit.SECONDS,deque);;
    private static ArrayBlockingQueue<Runnable> poolQeque = new ArrayBlockingQueue<Runnable>(10);
    private static ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(
                5,20, 5, TimeUnit.SECONDS,poolQeque);

    private Logger logger= LoggerFactory.getLogger(ThreadPoolExecutorService.class);
    public ThreadPoolExecutorService(){
        poolExecutor.allowCoreThreadTimeOut(true);
        singleExecutor.allowCoreThreadTimeOut(true);
    }

    @Override
    public void singleExecute(Runnable runnable){
        singleExecutor.execute(() -> {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            runnable.run();
        });
    }

    @Override
    public void execute(Runnable runnable){
        poolExecutor.execute(() -> {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
        } catch (InterruptedException e) {
            logger.error("销毁线程池",e);
        }
        logger.info("销毁线程池");
    }

}
