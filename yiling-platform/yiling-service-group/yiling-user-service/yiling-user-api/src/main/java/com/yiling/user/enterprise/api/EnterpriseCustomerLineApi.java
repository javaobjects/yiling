package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.dto.EnterpriseCustomerLineDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerLineListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCustomerLineRequest;

/**
 * 企业客户使用产品线 API
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
public interface EnterpriseCustomerLineApi {

    /**
     * 添加企业客户产品线
     *
     * @param request
     * @return
     */
    boolean add(List<AddCustomerLineRequest> request);

    /**
     * 删除企业客户产品线
     *
     * @param idList
     * @param opUserId
     * @return
     */
    boolean delete(List<Long> idList, Long opUserId);

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<EnterpriseCustomerLineDTO> queryList(QueryCustomerLineListRequest request);

    /**
     * 修改使用产品线
     *
     * @param request
     * @return
     */
    boolean updateLine(UpdateEnterpriseCustomerLineRequest request);

    /**
     * 查询是否开通指定类型的企业客户产品线
     *
     * @param eid
     * @param customerEid
     * @param lineEnum
     * @return
     */
    boolean getCustomerLineFlag(Long eid, Long customerEid, EnterpriseCustomerLineEnum lineEnum);

    /**
     * 查询是否开通指定类型的企业客户产品线
     *
     * @param eidList 采购商企业ID集合
     * @param customerEid 供应商企业ID
     * @param lineEnum 产品线枚举
     * @return key为eid，value为是否开通此产品线
     */
    Map<Long, Boolean> getCustomerLineListFlag(List<Long> eidList, Long customerEid, EnterpriseCustomerLineEnum lineEnum);


}
