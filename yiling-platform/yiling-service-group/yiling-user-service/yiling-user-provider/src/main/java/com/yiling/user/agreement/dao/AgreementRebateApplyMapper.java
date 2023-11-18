package com.yiling.user.agreement.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.agreement.dto.ApplyEntDTO;
import com.yiling.user.agreement.dto.request.QueryApplyEntPageListRequest;
import com.yiling.user.agreement.entity.AgreementApplyDO;

/**
 * <p>
 * 返利申请表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Repository
public interface AgreementRebateApplyMapper extends BaseMapper<AgreementApplyDO> {

	/**
	 * 根据申请单id修改申请单金额
	 *
	 * @param id
	 * @param amount
	 * @return
	 */
	int updateTotalAmountById(@Param("id") Long id, @Param("amount") BigDecimal amount);


	/**
	 * 查询已申请的返利入账客户
	 *
	 * @param page
	 * @param request opUserId不为空时查询createUser为opUserId的数据
	 * @return
	 */
	Page<ApplyEntDTO> queryApplyEntPageList(Page page, @Param("request") QueryApplyEntPageListRequest request);
}
