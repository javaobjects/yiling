package com.yiling.sales.assistant.commissions.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.CommissionsPayRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.RemoveCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.UpdateCommissionsEffectiveRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDO;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;

/**
 * <p>
 * 销售助手佣金记录表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
public interface CommissionsService extends BaseService<CommissionsDO> {

	/**
	 * 为用户新增佣金记录
	 *
	 * @param request
	 * @return
	 */
	Boolean addCommissionsToUser(AddCommissionsToUserRequest request);

    /**
     * 为用户新增佣金记录
     *
     * @param request
     * @return
     */
    Boolean addCommissionsToUserConsumer(AddCommissionsToUserRequest request);

    /**
     * 为用户删除佣金记录
     *  仅限佣金生效前
     *
     * @param request
     * @return
     */
    Boolean removeCommissionsToUser(RemoveCommissionsToUserRequest request);

	/**
	 * 使用户佣金生效
	 *
	 * @param request
	 * @return
	 */
	Boolean updateCommissionsEffective(UpdateCommissionsEffectiveRequest request);

    /**
     * 更新邀请人的佣金
     * @param request
     */
    void updateInviterCommission(UpdateCommissionsEffectiveRequest request);

	/**
	 * 根据用户id、用户任务id查询佣金记录
	 *
	 * @param userId
	 * @param userTaskId
	 * @return
	 */
	List<CommissionsDTO> queryCommissionsByUserTaskId(Long userId,Long userTaskId);

	/**
	 * 查询用户佣金记录
	 *
	 * @param userId   用户id
	 * @param typeEnum 收支类型，空为全部
	 * @param day      day天之前，空则不限制
	 * @return
	 */
	List<CommissionsDTO> queryCommissionsList(Long userId, CommissionsTypeEnum typeEnum, Integer day);

	/**
	 * 分页查询收支明细
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsDTO> queryCommissionsPageList(QueryCommissionsPageListRequest request);

	/**
	 * 佣金兑付
	 *
	 * @param request
	 * @return
	 */
	Boolean commissionsPay(List<CommissionsPayRequest> request);

}
