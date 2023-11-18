package com.yiling.framework.common.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * BaseServiceImpl
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public int deleteByIdWithFill(T entity) {
        return this.getBaseMapper().deleteByIdWithFill(entity);
    }

    @Override
    public int batchDeleteWithFill(T entity, Wrapper<T> wrapper) {
        return this.getBaseMapper().batchDeleteWithFill(entity,wrapper);
    }

    /**
     * 通常情况禁止使用物理删除方法<br/>
     * 请使用 deleteByIdWithFill 进行操作<br/>
     */
    @Deprecated
    @Override
    public boolean removeById(Serializable id) {
        log.warn("通常情况禁止使用物理删除方法");
        return SqlHelper.retBool(getBaseMapper().deleteById(id));
    }

    /**
     * 通常情况禁止使用物理删除方法<br/>
     * 请使用 batchDeleteWithFill 进行操作<br/>
     */
    @Deprecated
    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        log.warn("通常情况禁止使用物理删除方法");
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.retBool(getBaseMapper().deleteByMap(columnMap));
    }

    /**
     * 通常情况禁止使用物理删除方法<br/>
     * 请使用 batchDeleteWithFill 进行操作<br/>
     */
    @Deprecated
    @Override
    public boolean remove(Wrapper<T> queryWrapper) {
        log.warn("通常情况禁止使用物理删除方法");
        return SqlHelper.retBool(getBaseMapper().delete(queryWrapper));
    }

    /**
     * 通常情况禁止使用物理删除方法<br/>
     * 请使用 batchDeleteWithFill 进行操作<br/>
     */
    @Deprecated
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        log.warn("通常情况禁止使用物理删除方法");
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        return SqlHelper.retBool(getBaseMapper().deleteBatchIds(idList));
    }
}
