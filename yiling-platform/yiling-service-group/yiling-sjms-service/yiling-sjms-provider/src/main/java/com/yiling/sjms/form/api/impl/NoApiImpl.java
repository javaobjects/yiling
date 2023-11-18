package com.yiling.sjms.form.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.no.api.NoApi;
import com.yiling.basic.no.enums.INoEnum;
import com.yiling.sjms.gb.service.NoService;

/**
 * @author: gxl
 * @date: 2023/2/28
 */
@DubboService
public class NoApiImpl implements NoApi {
    @Autowired
    private NoService noService;
    @Override
    public String gen(INoEnum noEnum) {
        return noService.genNo(noEnum);
    }
}