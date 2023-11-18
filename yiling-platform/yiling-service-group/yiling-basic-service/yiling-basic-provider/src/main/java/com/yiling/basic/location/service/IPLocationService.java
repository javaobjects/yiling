package com.yiling.basic.location.service;

import org.springframework.stereotype.Service;

import com.yiling.basic.location.bo.IPLocationBO;

/**
 * IP归属地 Service
 *
 * @author: xuan.zhou
 * @date: 2022/10/19
 */
@Service
public interface IPLocationService {

    /**
     * IP归属地查询方法
     *
     * @param ip IP地址
     * @return
     */
    IPLocationBO query(String ip);

    /**
     * 调用查询方法验证IP归属地接口是否正确
     *
     * @return
     */
    void check();
}
