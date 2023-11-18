package com.yiling.dataflow.sjms.service;

import java.util.List;

import com.yiling.dataflow.sjms.entity.SjmsProvinceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 数据洞察系统省份对照关系 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
public interface SjmsProvinceService extends BaseService<SjmsProvinceDO> {

    /**
     * 获取省区对应的省份信息列表
     *
     * @param bizProvinceNames 省区名称列表
     * @return java.util.List<SjmsProvinceDO> 省份信息列表(Location表)
     * @author xuan.zhou
     * @date 2023/3/27
     **/
    List<SjmsProvinceDO> listByBizProvinceName(List<String> bizProvinceNames);
}
