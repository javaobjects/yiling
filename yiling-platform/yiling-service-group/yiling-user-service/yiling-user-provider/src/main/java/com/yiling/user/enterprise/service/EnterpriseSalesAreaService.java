package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDO;

/**
 * <p>
 * 企业销售区域 服务类
 * </p>
 *
 * @author zhouxuan
 * @date 2021-10-29
 */
public interface EnterpriseSalesAreaService extends BaseService<EnterpriseSalesAreaDO> {

    /**
     * 获取企业销售区域配置
     *
     * @param eid 企业ID
     * @return
     */
    EnterpriseSalesAreaDO getByEid(Long eid);

    /**
     * 批量获取企业销售区域配置
     *
     * @param eids 企业ID列表
     * @return
     */
    List<EnterpriseSalesAreaDO> listByEids(List<Long> eids);

    /**
     * 保存销售区域Json
     * @param eid
     * @param areaJsonString
     * @param opUserId
     * @return
     */
    Boolean save(Long eid , String areaJsonString ,Long opUserId);
}
