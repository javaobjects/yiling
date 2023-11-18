package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseChannelCustomerDTO;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;

/**
 * 渠道商 API
 *
 * @author: yuecheng.chen
 * @date: 2021/6/4 0004
 */
public interface ChannelCustomerApi {

    /**
     * 查询渠道商分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseChannelCustomerDTO> pageList(QueryChannelCustomerPageListRequest request);

    /**
     * 查询渠道商列表
     *
     * @param request
     * @return
     */
    List<EnterpriseChannelCustomerDTO> queryChannelCustomerList(QueryChannelCustomerPageListRequest request);

    /**
     * 获取渠道商详情
     *
     * @param eid
     * @param customerEid
     * @return
     */
    EnterpriseChannelCustomerDTO get(Long eid, Long customerEid);

    /**
     * 统计企业下客户数量
     *
     * @param eids 企业ID列表
     * @return key：企业ID value：客户数据
     */
    Map<Long, Long> countCustomersByEids(List<Long> eids);

}
