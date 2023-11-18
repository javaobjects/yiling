package com.yiling.mall.settlement.b2b.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.settlement.b2b.service.SettlementService;
import com.yiling.payment.enums.PaySettFeeChargeSideEnum;
import com.yiling.payment.enums.PaySettReceiveTypeEnum;
import com.yiling.payment.pay.api.PayTransferApi;
import com.yiling.payment.pay.dto.request.CreatePaymentTransferRequest;
import com.yiling.settlement.b2b.api.ReceiptAccountApi;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.request.CompensateSettlementPayStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementByIdRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.enums.ReceiptAccountStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementErrorCode;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;
import com.yiling.settlement.b2b.enums.SettlementStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * b2b商家结算单表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Service
@Slf4j
public class SettlementServiceImpl implements SettlementService {

	@DubboReference
	SettlementApi      settlementApi;
	@DubboReference
	ReceiptAccountApi  receiptAccountApi;
	@DubboReference
	PayTransferApi     payTransferApi;
	@DubboReference
	SettlementOrderApi settlementOrderApi;
	@DubboReference
	EnterpriseApi      enterpriseApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

	@Autowired
	StringRedisTemplate     stringRedisTemplate;
	@Autowired
	RedisDistributedLock    redisDistributedLock;
	@Autowired
    @Lazy
    SettlementServiceImpl    _this;



	@Override
	@GlobalTransactional
	public Boolean submitPayment(List<Long> settlementIdList, Long opUserId) {

		List<SettlementDTO> settlementDTOList = settlementApi.getByIdList(settlementIdList);
		// 上锁
		lockSettlement(settlementDTOList);

		//检查参数
		List<ReceiptAccountDTO> accountList = checkLegal(settlementDTOList);

		Map<Long, ReceiptAccountDTO> accountDTOMap = accountList.stream().collect(Collectors.toMap(ReceiptAccountDTO::getEid, e -> e));
		//todo 调用打款功能
		CreatePaymentTransferRequest request = new CreatePaymentTransferRequest();
		List<CreatePaymentTransferRequest.PaymentTransferRequest> transferRequestList = ListUtil.toList();
		request.setTransferRequestList(transferRequestList);
		request.setOpUserId(opUserId);
		settlementDTOList.forEach(e -> {
			CreatePaymentTransferRequest.PaymentTransferRequest var = new CreatePaymentTransferRequest.PaymentTransferRequest();
			var.setEid(Constants.YILING_EID);
			var.setBusinessId(e.getId());
			var.setAmount(e.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
			var.setPayChannel(1);
			var.setFeeChargeSide(PaySettFeeChargeSideEnum.PLATFORM.getCode());
			var.setReceiveType(PaySettReceiveTypeEnum.SHORTLY.getCode());
			ReceiptAccountDTO receiptAccountDTO = accountDTOMap.get(e.getEid());
			var.setSellerEid(receiptAccountDTO.getEid());
			var.setAccount(receiptAccountDTO.getAccount());
			var.setAccountName(receiptAccountDTO.getName());
			var.setBankCode(receiptAccountDTO.getBankCode());
			var.setBranchCode(receiptAccountDTO.getBranchNum());
			var.setReceiptAccountId(receiptAccountDTO.getId());
			transferRequestList.add(var);
		});
		Result<Map<Long,String>> payTransfer = payTransferApi.createPayTransfer(request);
		if (ObjectUtil.notEqual(payTransfer.getCode(), ResultCode.SUCCESS.getCode())) {
			log.error("调用支付通道打款失败");
			return Boolean.FALSE;
		}
        Map<Long, String> paymentTransferResultDTOMap = payTransfer.getData();

        //打款成功和失败的结算单
		List<UpdateSettlementStatusRequest> successSettList = ListUtil.toList();
		List<UpdateSettlementStatusRequest> failSettList = ListUtil.toList();

		//生成待更新的结算单
		List<UpdateSettlementByIdRequest> updateList = ListUtil.toList();
		settlementDTOList.forEach(settlementDO -> {
			UpdateSettlementByIdRequest var = new UpdateSettlementByIdRequest();
			//准备更新结算单订单的数据
			UpdateSettlementStatusRequest var2 = PojoUtils.map(settlementDO, UpdateSettlementStatusRequest.class);
			//todo 设置payNo
			String payNo = paymentTransferResultDTOMap.get(settlementDO.getId());
			var.setId(settlementDO.getId());
			var.setOpUserId(opUserId);
			var.setBankReceiptId(accountDTOMap.get(settlementDO.getEid()).getId());
			var.setSettlementTime(new Date());
            var.setPayNo(payNo);
            var.setStatus(SettlementStatusEnum.BANK_COPE.getCode());
            var.setPaySource(1);
            successSettList.add(var2);
//			//如果发起打款成功
//			if (resultDTO.getCreateStatus()) {
//				var.setPayNo(resultDTO.getPayNo());
//				var.setErrMsg("");
//				if (resultDTO.getCreateStatus()) {
//					var.setStatus(SettlementStatusEnum.BANK_COPE.getCode());
//				} else {
//					var.setStatus(SettlementStatusEnum.FAIL.getCode());
//				}
//				var.setPaySource(1);
//				var.setThirdPayNo(resultDTO.getThirdTradeNo());
//				successSettList.add(var2);
//			} else {
//				var.setStatus(SettlementStatusEnum.FAIL.getCode());
//				var.setErrMsg(resultDTO.getErrMSg());
//				failSettList.add(var2);
//			}
			updateList.add(var);
		});
		//更新数据
		Boolean isUpdate = updateSettlement(updateList, successSettList, failSettList);
		//更新成功
		if (isUpdate) {
			//释放锁
			unLockSettlement(settlementIdList);
		}
		return isUpdate;
	}

	/**
	 * 检查单据中是否存在非法单据
	 *
	 * @param settlementDTOList
	 */
	public List<ReceiptAccountDTO> checkLegal(List<SettlementDTO> settlementDTOList) {
		List<Long> settlementIdList = settlementDTOList.stream().map(SettlementDTO::getId).collect(Collectors.toList());

		//检查结状态是否合法
		List<SettlementDTO> invalidSettlementList = settlementDTOList.stream().filter(e ->
				ObjectUtil.equal(e.getStatus(), SettlementStatusEnum.SETTLE.getCode()) || ObjectUtil.equal(e.getStatus(),
						SettlementStatusEnum.BANK_COPE.getCode())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(invalidSettlementList)) {
			log.warn("发起结算的结算单中包含非法的结算单状态，结算单号：{}", CollUtil.join(invalidSettlementList.stream()
					.map(SettlementDTO::getCode).collect(Collectors.toList()), ","));
			//释放锁
			unLockSettlement(settlementIdList);
			throw new BusinessException(SettlementErrorCode.SETTLEMENT_STATUS_INVALID, "结算单已发起打款，请刷新页面");
		}
		//查询企业收款账户
		List<Long> eidList = settlementDTOList.stream().map(SettlementDTO::getEid).distinct().collect(Collectors.toList());
		List<ReceiptAccountDTO> accountList = receiptAccountApi.queryValidReceiptAccountByEidList(eidList);
		//如果企业收款账户数量和eidList不一致
		if (!ObjectUtil.equal(eidList.size(), accountList.size())) {
			List<Long> existenceEid = accountList.stream().map(ReceiptAccountDTO::getEid).collect(Collectors.toList());
			List<Long> unExistList = CollUtil.subtractToList(eidList, existenceEid);
			//释放锁
			unLockSettlement(settlementIdList);
			log.warn("发起结算的结算单中包含没有企业收款账户的企业，eid：{}", CollUtil.join(CollUtil.subtractToList(eidList, existenceEid), ","));
			throw new BusinessException(SettlementErrorCode.ACCOUNT_NOT_FOUND,
					SettlementErrorCode.ACCOUNT_NOT_FOUND.getMessage() + getEntNameByIds(CollUtil.subtractToList(eidList, existenceEid)));
		}

		//校验收款账户审核状态
		List<ReceiptAccountDTO> accountInvalid = accountList.stream().filter(e -> ObjectUtil.notEqual(e.getStatus(),
				ReceiptAccountStatusEnum.SUCCESS.getCode())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(accountInvalid)) {
			log.error("企业首款在账户正在审核或已驳回，收款账户id：{}", CollUtil.join(accountInvalid.stream().map(ReceiptAccountDTO::getId)
					.collect(Collectors.toList()), "，"));
			//释放锁
			unLockSettlement(settlementIdList);
			throw new BusinessException(SettlementErrorCode.ACCOUNT_CHECK_IN, "企业收款账户未审核通过，请确认后再操作：" +
					getEntNameByIds(accountInvalid.stream().map(ReceiptAccountDTO::getEid).collect(Collectors.toList())));
		}
		return accountList;
	}

	/**
	 * 更新结算单等状态
	 *
	 * @param updateList
	 * @param successSettList
	 * @param failSettList
	 * @return
	 */
	public Boolean updateSettlement(List<UpdateSettlementByIdRequest> updateList, List<UpdateSettlementStatusRequest> successSettList, List<UpdateSettlementStatusRequest> failSettList) {
		//更新结算单状态
		Boolean isSuccess = settlementApi.updateSettlementById(updateList);
		if (!isSuccess) {
			String msgKey = UUID.fastUUID().toString();
			log.error("发起打款时提交支付公司成功，但更新结算单状态失败，向MQ发送消息进行更新结算单状态补偿，参数={}，megKey={}", updateList, msgKey);
			CompensateSettlementPayStatusRequest compensateRequest = new CompensateSettlementPayStatusRequest();
			compensateRequest.setUpdateList(updateList);
			compensateRequest.setSuccessSettList(successSettList);
			compensateRequest.setFailSettList(failSettList);
            _this.sendMq(Constants.TOPIC_B2B_SETTLEMENT_STATUS_NOTIFY,"",JSON.toJSONString(compensateRequest));
			throw new BusinessException(SettlementErrorCode.UPDATE_SETTLEMENT_STATUS_FAIL);
		}
		//更新结算单订单状态
		isSuccess = settlementOrderApi.updateSettlementStatus(successSettList, failSettList, SettlementOperationTypeEnum.SEND);

		//删除打款失败的redisKey
		Stream<UpdateSettlementByIdRequest> sendFailSettList = updateList.stream().filter(e ->
				ObjectUtil.equal(SettlementStatusEnum.FAIL.getCode(), e.getStatus()));
		unLockSettlement(sendFailSettList.map(UpdateSettlementByIdRequest::getId).collect(Collectors.toList()));
		return isSuccess;
	}

	public void lockSettlement(List<SettlementDTO> settlementIdList) {
		// 防止重复打款
		settlementIdList.forEach(e -> {
			String key = new StringBuilder("b2b_settlement_payment").append(":").append(e.getId().toString()).toString();
			if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
				boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, e.getCode(), 60*60*3, TimeUnit.SECONDS);
				if (!result) {
					throw new BusinessException(ResultCode.FAILED, "结算单号为" + e.getCode() + "的结算单正在处理中，请稍后刷新页面重试或者联系技术人员处理");
				}
			} else {
				throw new BusinessException(ResultCode.FAILED, "结算单号为" + e.getCode() + "的结算单正在处理中，请稍后刷新页面重试或者联系技术人员处理");
			}
		});
	}

	public Boolean unLockSettlement(List<Long> settlementIdList) {
		settlementIdList.forEach(e -> {
			String key = new StringBuilder("b2b_settlement_payment").append(":").append(e.toString()).toString();
			try {
				Boolean isDelete = stringRedisTemplate.delete(key);
				if (!isDelete) {
					log.error("结算单释放锁失败，key：{}", key);
				}
			} catch (Exception exception) {
				log.error("结算单释放锁失败，key：{}", key);
			}
		});
		return Boolean.TRUE;
	}


	/**
	 * 根据企业id查询企业名称
	 *
	 * @param idList
	 * @return
	 */
	public String getEntNameByIds(List<Long> idList) {
		List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(idList);
		List<String> nameList = enterpriseDTOS.stream().map(EnterpriseDTO::getName).collect(Collectors.toList());
		return CollUtil.join(nameList, "，");
	}

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);

        mqMessageSendApi.send(mqMessageBO);

        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {

        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }


}
