package com.yiling.erp.client.queue;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/10/9
 */
public interface LocalMessageQueueService<E> {

    void put(E e) throws InterruptedException;

    E take() throws InterruptedException;

    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    E poll(long timeout, TimeUnit unit) throws InterruptedException;

    int size();
}
