package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerLineListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCustomerLineRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerLineDO;

/**
 * <p>
 * 企业客户使用产品线 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021-11-29
 */
public interface EnterpriseCustomerLineService extends BaseService<EnterpriseCustomerLineDO> {

    /**
     * 添加企业客户产品线
     * @param request
     * @return
     */
    boolean add(List<AddCustomerLineRequest> request);

    /**
     * 删除企业客户产品线
     * @param idList
     * @return
     */
    boolean delete(List<Long> idList ,Long opUserId);

    /**
     * 根据企业客户ID删除企业客户产品线
     *
     * @param customerId
     * @param opUserId
     * @return
     */
    boolean deleteByCustomerId(Long customerId ,Long opUserId);

    /**
     * 列表查询
     * @param request
     * @return
     */
    List<EnterpriseCustomerLineDO> queryList(QueryCustomerLineListRequest request);

    /**
     * 更新企业客户产品线
     * @param request
     * @return
     */
    boolean updateLine(UpdateEnterpriseCustomerLineRequest request);

    /**
     * 查询是否开通指定类型的企业客户产品线
     * @param eid
     * @param customerEid
     * @param lineEnum
     * @return
     */
    boolean getCustomerLineFlag(Long eid, Long customerEid, EnterpriseCustomerLineEnum lineEnum);

    /**
     * 查询是否开通指定类型的企业客户产品线
     * @param eidList
     * @param customerEid
     * @param lineEnum
     * @return
     */
    Map<Long, Boolean> getCustomerLineListFlag(List<Long> eidList, Long customerEid, EnterpriseCustomerLineEnum lineEnum);

}
