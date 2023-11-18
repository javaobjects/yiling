package com.yiling.user.enterprise.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.AddCustomerEasInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;

/**
 * <p>
 * 企业客户对应的eas信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-26
 */
public interface EnterpriseCustomerEasService extends BaseService<EnterpriseCustomerEasDO> {

	/**
	 * 查询客户EAS信息分页列表
	 *
	 * @param request
	 * @return
	 */
	Page<EnterpriseCustomerEasDO> pageList(QueryCustomerEasInfoPageListRequest request);

	/**
	 * 获取商务联系人负责的企业EAS信息分页列表
	 *
	 * @param request
	 * @return
	 */
	Page<EnterpriseCustomerEasDO> pageListByCurrent(QueryCustomerEasInfoPageListByCurrentRequest request);


	/**
	 * 获取客户EAS信息列表
	 *
	 * @param eid         企业ID
	 * @param customerEid 客户ID
	 * @return
	 */
	List<EnterpriseCustomerEasDO> listByCustomer(Long eid, Long customerEid);

	/**
	 * 批量获取客户EAS信息列表
	 *
	 * @param eid          企业ID
	 * @param customerEids 客户ID列表
	 * @return key：客户ID，value：客户EAS信息列表
	 */
	Map<Long, List<EnterpriseCustomerEasDO>> listCustomerEasInfos(Long eid, List<Long> customerEids);

	/**
	 * 通过企业ID和easCode查询对应的企业信息
	 *
	 * @param eid     企业ID
	 * @param easCode eas内码
	 * @return
	 */
	Long getCustomerEidByEasCode(Long eid, String easCode);

	/**
	 * 根据企业id及easCode更新兑付金额
	 *
	 * @param eid
	 * @param easCode
	 * @param amount
	 * @return
	 */
	Boolean updateAppliedAmount(Long eid, String easCode, BigDecimal amount);

    /**
     * 添加客户EAS信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/5/25
     **/
    Boolean add(AddCustomerEasInfoRequest request);
}
