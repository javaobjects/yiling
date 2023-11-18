package com.yiling.open.ftp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.ftp.entity.FlowFtpCacheDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-25
 */
public interface FlowFtpCacheService extends BaseService<FlowFtpCacheDO> {

    /**
     * 添加缓存对象
     * @param list
     * @param suId
     * @param taskNo
     */
     void addCache(List<BaseErpEntity> list, Long suId,String taskNo);

    /**
     * 修改缓存对象
     * @param list
     * @param suId
     * @param taskNo
     */
    void updateCache(List<BaseErpEntity> list, Long suId,String taskNo);

    /**
     * 删除缓存对象
     * @param list
     * @param suId
     * @param taskNo
     */
    void deleteCache(List<BaseErpEntity> list, Long suId,String taskNo);
}
