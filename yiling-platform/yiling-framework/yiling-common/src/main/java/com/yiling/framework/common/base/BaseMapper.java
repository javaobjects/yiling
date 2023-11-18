package com.yiling.framework.common.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * BaseMapper
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    int deleteByIdWithFill(T entity);

    int batchDeleteWithFill(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    int insertBatchSomeColumn(@Param("list") List<T> batchList);
}
