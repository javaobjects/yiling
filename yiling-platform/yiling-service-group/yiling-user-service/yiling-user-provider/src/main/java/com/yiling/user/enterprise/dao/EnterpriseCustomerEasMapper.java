package com.yiling.user.enterprise.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;

/**
 * <p>
 * 企业客户对应的eas信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-26
 */
@Repository
public interface EnterpriseCustomerEasMapper extends BaseMapper<EnterpriseCustomerEasDO> {

	/**
	 * 查询客户EAS信息分页列表
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	Page<EnterpriseCustomerEasDO> pageList(Page page, @Param("request") QueryCustomerEasInfoPageListRequest request);

	/**
	 * 获取商务联系人负责的企业EAS信息分页列表
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	Page<EnterpriseCustomerEasDO> pageListByCurrent(Page page, @Param("request") QueryCustomerEasInfoPageListByCurrentRequest request);

	/**
	 * 根据企业id及easCode更新兑付金额
	 *
	 * @param eid
	 * @param easCode
	 * @param amount
	 * @return
	 */
	int updateAppliedAmount(@Param("eid") Long eid, @Param("easCode") String easCode, @Param("amount") BigDecimal amount);
}
