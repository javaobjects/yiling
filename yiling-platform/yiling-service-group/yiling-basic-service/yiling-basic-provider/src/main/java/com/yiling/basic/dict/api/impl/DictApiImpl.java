package com.yiling.basic.dict.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.dict.service.DictService;

/**
 * 数据字典 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@DubboService
public class DictApiImpl implements DictApi {

    @Autowired
    DictService dictService;

    @Override
    public List<DictBO> getEnabledList() {
        return dictService.getEnabledList();
    }

    @Override
    public DictBO getDictByName(String name) {
        return dictService.getDictByName(name);
    }
}
