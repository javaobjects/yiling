package com.yiling.basic.dict.api;

import java.util.List;

import com.yiling.basic.dict.bo.DictBO;

/**
 * 数据字典 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
public interface DictApi {

    List<DictBO> getEnabledList();

    DictBO getDictByName(String name);
}
