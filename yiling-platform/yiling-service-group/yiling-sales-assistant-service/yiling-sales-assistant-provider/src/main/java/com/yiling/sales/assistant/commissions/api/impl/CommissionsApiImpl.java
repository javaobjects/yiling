package com.yiling.sales.assistant.commissions.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserStatisticsDTO;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.CommissionsPayRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.RemoveCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.UpdateCommissionsEffectiveRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDO;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.commissions.service.CommissionsUserService;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author dexi.yao
 * @date 2021-09-17
 */
@DubboService
public class CommissionsApiImpl implements CommissionsApi {

	@Autowired
	CommissionsService     commissionsService;
	@Autowired
	CommissionsUserService commissionsUserService;

	@Override
	public CommissionsDTO queryById(Long id) {
		CommissionsDO commissionsDO = commissionsService.getById(id);
		return PojoUtils.map(commissionsDO, CommissionsDTO.class);
	}

	@Override
	public Boolean addCommissionsToUser(AddCommissionsToUserRequest request) {
		return commissionsService.addCommissionsToUser(request);
	}

    @Override
	public Boolean updateCommissionsEffective(UpdateCommissionsEffectiveRequest request) {
		return commissionsService.updateCommissionsEffective(request);
	}

	@Override
	public CommissionsUserStatisticsDTO commissionsUserStatistics(Long userId) {

		BigDecimal yesterday = BigDecimal.ZERO;
		BigDecimal week = BigDecimal.ZERO;
		BigDecimal month = BigDecimal.ZERO;

		List<CommissionsDTO> commissionsDTOS = commissionsService.queryCommissionsList(userId, CommissionsTypeEnum.INPUT, 30);
		//昨天开始时间
		DateTime yesterdayTime = DateUtil.beginOfDay(DateUtil.yesterday());
		//七日开始时间
		DateTime weekTime = DateUtil.offsetDay(yesterdayTime, Math.negateExact(7));
		//七日开始时间
		DateTime monthTime = DateUtil.offsetDay(yesterdayTime, Math.negateExact(30));
		//统计收益
		for (CommissionsDTO item : commissionsDTOS) {
			if (item.getEffectTime().compareTo(yesterdayTime) == 1) {
				yesterday = yesterday.add(item.getAmount());
			}
			if (item.getEffectTime().compareTo(weekTime) == 1) {
				week = week.add(item.getAmount());
			}
			if (item.getEffectTime().compareTo(monthTime) == 1) {
				month = month.add(item.getAmount());
			}
		}
		//查询用户余额
		CommissionsUserDTO commissionsUserDTO = commissionsUserService.queryCommissionsUserByUserId(userId);

		CommissionsUserStatisticsDTO result = new CommissionsUserStatisticsDTO();
		result.setYesterdayAmount(yesterday);
		result.setWeekAmount(week);
		result.setMonthAmount(month);
		result.setSurplusAmount(commissionsUserDTO.getSurplusAmount());
		return result;
	}

	@Override
	public Page<CommissionsDTO> queryCommissionsPageList(QueryCommissionsPageListRequest request) {
		return commissionsService.queryCommissionsPageList(request);
	}

	@Override
	public Boolean commissionsPay(List<CommissionsPayRequest> request) {
		return commissionsService.commissionsPay(request);
	}

}
