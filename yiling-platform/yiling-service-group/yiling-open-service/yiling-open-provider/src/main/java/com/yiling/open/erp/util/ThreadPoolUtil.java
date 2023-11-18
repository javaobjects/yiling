package com.yiling.open.erp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

    private static final ExecutorService pool = Executors.newFixedThreadPool(50);

    public static void submit(Runnable runnable){
        pool.submit(runnable);
    }
}
