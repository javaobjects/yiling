package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.TicketDiscountRecordMapper;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;
import com.yiling.order.order.entity.TicketDiscountRecordDO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.service.TicketDiscountRecordService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 票折信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Service
public class TicketDiscountRecordServiceImpl extends BaseServiceImpl<TicketDiscountRecordMapper, TicketDiscountRecordDO> implements TicketDiscountRecordService {

	@Override
	public List<TicketDiscountRecordDO> listCustomerTicketDiscounts(String sellerErpCode, String customerErpCode) {
		QueryWrapper<TicketDiscountRecordDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(TicketDiscountRecordDO::getSellerErpCode, sellerErpCode)
				.eq(TicketDiscountRecordDO::getCustomerErpCode, customerErpCode)
				.gt(TicketDiscountRecordDO::getAvailableAmount, 0)
				.eq(TicketDiscountRecordDO::getStatus, EnableStatusEnum.ENABLED.getCode())
				.orderByAsc(TicketDiscountRecordDO::getCreateTime);

		List<TicketDiscountRecordDO> list = this.list(queryWrapper);
		if (CollUtil.isEmpty(list)) {
			return ListUtil.empty();
		}

		return list;
	}

	/**
	 * 根据票折单号，获取对应EAS传过来票折信息
	 *
	 * @param ticketDiscountNo
	 * @return
	 */
	@Override
	public TicketDiscountRecordDTO getTicketDiscountRecordByTicketNo(String ticketDiscountNo) {
		QueryWrapper<TicketDiscountRecordDO> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TicketDiscountRecordDO::getTicketDiscountNo, ticketDiscountNo)
				.last(" limit 1 ");
		TicketDiscountRecordDO ticketDiscountRecord = getOne(wrapper);
		if (ticketDiscountRecord == null) {
			throw new BusinessException(OrderErrorCode.ORDER_THAN_TICKET_DISCOUNT_NOT_EXISTS);
		}
		return PojoUtils.map(ticketDiscountRecord, TicketDiscountRecordDTO.class);
	}

	/**
	 * 根据单号查询票折信息
	 *
	 * @param ticketNos 批量票折单号
	 * @return
	 */
	@Override
	public List<TicketDiscountRecordDTO> getTicketDiscountRecordByTicketNoList(List<String> ticketNos) {
		QueryWrapper<TicketDiscountRecordDO> wrapper = new QueryWrapper<>();
		wrapper.lambda().in(TicketDiscountRecordDO::getTicketDiscountNo, ticketNos)
                .gt(TicketDiscountRecordDO :: getAvailableAmount,0)
				.orderByAsc(TicketDiscountRecordDO::getCreateTime);
		return PojoUtils.map(list(wrapper), TicketDiscountRecordDTO.class);
	}

	/**
	 * 修改使用票折金额
	 *
	 * @param ticketDiscountNo 票折单号
	 * @param usedAmount       需要锁定的金额，释放金额为负
	 * @return
	 */
	@Override
	public Boolean updateUsedAmount(String ticketDiscountNo, BigDecimal usedAmount) {
		this.getBaseMapper().updateUsedAmount(ticketDiscountNo, usedAmount);
		return true;
	}

	/**
	 * 修改使用票折金额
	 *
	 * @param ticketDiscountNo 票折单号
	 * @param usedAmount       需要锁定的金额，释放金额为负
	 * @return
	 */
	@Override
	public Boolean reduceUsedAmount(String ticketDiscountNo, BigDecimal usedAmount) {
		this.getBaseMapper().reduceUsedAmount(ticketDiscountNo, usedAmount);
		return true;
	}

	@Override
	public TicketDiscountRecordDTO saveOrUpdate(AddTicketDiscountRecordRequest recordRequest) {
		TicketDiscountRecordDO recordDTO = PojoUtils.map(recordRequest, TicketDiscountRecordDO.class);
		boolean isAdd = saveOrUpdate(recordDTO);
		if (!isAdd) {
			throw new BusinessException();
		}
		return PojoUtils.map(recordDTO, TicketDiscountRecordDTO.class);
	}

	@Override
	public Page<TicketDiscountRecordDTO> queryPageListByUseTime(QueryTicketDiscountPageListRequest request) {
		LambdaQueryWrapper<TicketDiscountRecordDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(StrUtil.isNotBlank(request.getTicketDiscountNo()), TicketDiscountRecordDO::getTicketDiscountNo, request.getTicketDiscountNo())
				.gt(TicketDiscountRecordDO::getUsedAmount, 0)
				.ge(TicketDiscountRecordDO::getUpdateTime, request.getStartTime())
				.le(TicketDiscountRecordDO::getUpdateTime, request.getEndTime());
		Page<TicketDiscountRecordDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, TicketDiscountRecordDTO.class);
	}
}
