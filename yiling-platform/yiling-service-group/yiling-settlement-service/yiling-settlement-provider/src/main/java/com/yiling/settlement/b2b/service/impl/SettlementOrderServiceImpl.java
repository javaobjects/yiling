package com.yiling.settlement.b2b.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.b2b.dao.SettlementOrderMapper;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementOrderDO;
import com.yiling.settlement.b2b.enums.SettlementErrorCode;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;
import com.yiling.settlement.b2b.enums.SettlementStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementTypeEnum;
import com.yiling.settlement.b2b.service.SettlementOrderService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * b2b商家结算单-订单对账表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-26
 */
@Service
@Slf4j
public class SettlementOrderServiceImpl extends BaseServiceImpl<SettlementOrderMapper, SettlementOrderDO> implements SettlementOrderService {

	@Override
	public Page<SettlementOrderDTO> querySettlementOrderPageList(QuerySettlementOrderPageListRequest request) {
		LambdaQueryWrapper<SettlementOrderDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(ObjectUtil.isNotNull(request.getSellerEid()), SettlementOrderDO::getSellerEid, request.getSellerEid());
		wrapper.eq(StrUtil.isNotBlank(request.getOrderNo()), SettlementOrderDO::getOrderNo, request.getOrderNo());
		wrapper.eq(ObjectUtil.isNotNull(request.getSaleStatus()) && ObjectUtil.notEqual(request.getSaleStatus(), 0), SettlementOrderDO::getSaleStatus, request.getSaleStatus());
		wrapper.eq(ObjectUtil.isNotNull(request.getGoodsStatus()) && ObjectUtil.notEqual(request.getGoodsStatus(), 0), SettlementOrderDO::getGoodsStatus, request.getGoodsStatus());
		wrapper.eq(ObjectUtil.isNotNull(request.getPresaleDefaultStatus()) && ObjectUtil.notEqual(request.getPresaleDefaultStatus(), 0), SettlementOrderDO::getPresaleDefaultStatus, request.getPresaleDefaultStatus());
		if (ObjectUtil.isNotNull(request.getMinCreateTime())){
			wrapper.ge( SettlementOrderDO::getCreateTime, DateUtil.beginOfDay(request.getMinCreateTime()));
		}
		if (ObjectUtil.isNotNull(request.getMaxCreateTime())){
			wrapper.le( SettlementOrderDO::getCreateTime, DateUtil.endOfDay(request.getMaxCreateTime()));
		}
		wrapper.orderByDesc(SettlementOrderDO::getId);
		Page<SettlementOrderDO> page = page(request.getPage(), wrapper);

		return PojoUtils.map(page, SettlementOrderDTO.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateSettlementStatus(List<UpdateSettlementStatusRequest> successList
			, List<UpdateSettlementStatusRequest> failList, SettlementOperationTypeEnum type) {

		if (ObjectUtil.isNull(type)){
			log.error("操作类型不正确");
			return Boolean.FALSE;
		}
		//初始化状态
		SettlementStatusEnum successStatusEnum;
		SettlementStatusEnum failStatusEnum;
		if (ObjectUtil.equal(type.getCode(),SettlementOperationTypeEnum.SEND.getCode())){
			successStatusEnum=SettlementStatusEnum.BANK_COPE;
			failStatusEnum=SettlementStatusEnum.FAIL;
		}else {
			successStatusEnum=SettlementStatusEnum.SETTLE;
			failStatusEnum=SettlementStatusEnum.FAIL;
		}
		//更新状态为成功的结算单
		if(CollUtil.isNotEmpty(successList)){
			Map<Integer, List<UpdateSettlementStatusRequest>> successSettMap = successList.stream()
					.collect(Collectors.groupingBy(UpdateSettlementStatusRequest::getType));
			successSettMap.forEach((settType, settlementDOList) -> {
				LambdaQueryWrapper<SettlementOrderDO> wrapper = Wrappers.lambdaQuery();
				SettlementOrderDO settlementOrderDO=new SettlementOrderDO();
				//如果是货款结算单
				if (ObjectUtil.equal(SettlementTypeEnum.GOODS.getCode(), settType)) {
					wrapper.in(SettlementOrderDO::getGoodsSettlementNo, settlementDOList.stream()
							.map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
					settlementOrderDO.setGoodsStatus(successStatusEnum.getCode());
				} else if (ObjectUtil.equal(SettlementTypeEnum.SALE.getCode(), settType)){
					//如果是促销结算单
					wrapper.in(SettlementOrderDO::getSaleSettlementNo, settlementDOList.stream()
							.map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
					settlementOrderDO.setSaleStatus(successStatusEnum.getCode());
				}else if (ObjectUtil.equal(SettlementTypeEnum.PRESALE_DEFAULT.getCode(), settType)){
                    //如果是预售违约结算单
                    wrapper.in(SettlementOrderDO::getPdSettlementNo, settlementDOList.stream()
                            .map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
                    settlementOrderDO.setPresaleDefaultStatus(successStatusEnum.getCode());
                }
				boolean isUpdate = update(settlementOrderDO, wrapper);
				if (!isUpdate){
					log.error("");
					throw new BusinessException(SettlementErrorCode.UPDATE_ORDER_SETTLEMENT_DETAIL_STATUS_FAIL);
				}
			});
		}

		//更细状态为失败的结算单
		if (CollUtil.isNotEmpty(failList)){
			Map<Integer, List<UpdateSettlementStatusRequest>> failSettMap = failList.stream().collect(Collectors.groupingBy(UpdateSettlementStatusRequest::getType));
			failSettMap.forEach((settType, settlementDOList) -> {
				LambdaQueryWrapper<SettlementOrderDO> wrapper = Wrappers.lambdaQuery();
				SettlementOrderDO settlementOrderDO=new SettlementOrderDO();
				//如果是货款结算单
				if (ObjectUtil.equal(SettlementTypeEnum.GOODS.getCode(), settType)) {
					wrapper.in(SettlementOrderDO::getGoodsSettlementNo, settlementDOList.stream()
							.map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
					settlementOrderDO.setGoodsStatus(failStatusEnum.getCode());
				} else if (ObjectUtil.equal(SettlementTypeEnum.SALE.getCode(), settType)){
					//如果是促销结算单
					wrapper.in(SettlementOrderDO::getSaleSettlementNo, settlementDOList.stream()
							.map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
					settlementOrderDO.setSaleStatus(failStatusEnum.getCode());
				} else if (ObjectUtil.equal(SettlementTypeEnum.PRESALE_DEFAULT.getCode(), settType)){
                    //如果是预售违约结算单
                    wrapper.in(SettlementOrderDO::getPdSettlementNo, settlementDOList.stream()
                            .map(UpdateSettlementStatusRequest::getCode).collect(Collectors.toList()));
                    settlementOrderDO.setPresaleDefaultStatus(failStatusEnum.getCode());
                }
				boolean isUpdate = update(settlementOrderDO, wrapper);
				if (!isUpdate){
					log.error("更新订单的结算状态失败");
					throw new BusinessException(SettlementErrorCode.UPDATE_ORDER_SETTLEMENT_DETAIL_STATUS_FAIL);
				}
			});
		}
		return Boolean.TRUE;
	}

    @Override
    public SettlementOrderDTO querySettOrderByOrderId(Long orderId) {
        if (ObjectUtil.isNull(orderId)){
            return null;
        }
        LambdaQueryWrapper<SettlementOrderDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SettlementOrderDO::getOrderId, orderId);
        SettlementOrderDO orderDO = getOne(wrapper);
        return PojoUtils.map(orderDO,SettlementOrderDTO.class);
    }

}
