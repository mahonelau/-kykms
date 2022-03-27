package org.jeecg.modules.KM.service;


public interface IThreadPoolExecutorService {
    void singleExecute(Runnable runnable);

    void execute(Runnable runnable);
}
