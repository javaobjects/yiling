package com.yiling.open.erp.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.yiling.open.erp.service.LocalMessageQueueService;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/10/9
 */
@Service
public class LocalMessageQueueServiceImpl<E> implements LocalMessageQueueService<E> {

    private static final int DEFAULT_CAPACITY = 10000;
    private BlockingQueue<E> blockingQueue;

    @PostConstruct
    private void init() {
        // 初始化队列
        this.blockingQueue = new LinkedBlockingDeque<>(DEFAULT_CAPACITY);
    }

    @Override
    public void put(E e) throws InterruptedException {
        this.blockingQueue.put(e);
    }

    @Override
    public E take() throws InterruptedException {
        return this.blockingQueue.take();
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return this.blockingQueue.offer(e, timeout, unit);
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return this.blockingQueue.poll(timeout, unit);
    }

    @Override
    public int size() {
        return this.blockingQueue.size();
    }
}
