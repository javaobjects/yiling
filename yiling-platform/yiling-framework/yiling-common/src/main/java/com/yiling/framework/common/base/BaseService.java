package com.yiling.framework.common.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * BaseService
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 根据ID进行逻辑删除，并填充相关字段
     *
     * @param entity
     * @return
     */
    int deleteByIdWithFill(T entity);

    int batchDeleteWithFill(T entity, Wrapper<T> wrapper);
}
