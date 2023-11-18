package com.yiling.basic.dict.service;

import java.util.List;

import com.yiling.basic.dict.bo.DictBO;

/**
 * 数据字典 Service
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
public interface DictService {

    /**
     * 获取所有数据字典
     *
     * @return
     */
    List<DictBO> getEnabledList();

    /**
     * 根据字典名获取字典对象
     * @param name 字典名
     * @return
     */
    DictBO getDictByName(String name);
}
