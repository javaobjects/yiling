package com.yiling.open.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.open.web.vo.OrderTicketDiscountVO;
import com.yiling.open.web.form.AddRebateUseForm;
import com.yiling.open.web.form.AuditRebateApplyForm;
import com.yiling.open.web.form.QueryRebatePageListForm;
import com.yiling.open.web.form.WithdrawRebateApplyForm;
import com.yiling.open.web.vo.RebatePageListItemVO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.api.TicketDiscountRecordApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;
import com.yiling.order.order.enums.TicketDiscountStatusEnum;
import com.yiling.user.agreement.api.AgreementRebateDictionariesApi;
import com.yiling.user.agreement.api.UseApi;
import com.yiling.user.agreement.api.UseDetailApi;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.request.AddUseDetailRequest;
import com.yiling.user.agreement.dto.request.AddUseRequest;
import com.yiling.user.agreement.enums.AgreementRestitutionTypeEnum;
import com.yiling.user.agreement.enums.AgreementUseStatusEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
@RestController
@Slf4j
@RequestMapping("/rebate")
public class RebateController {
	@DubboReference
	UseApi                         useApi;
	@DubboReference
	UseDetailApi                   useDetailApi;
	@DubboReference
	AgreementRebateDictionariesApi rebateDictionariesApi;
	@DubboReference
	LocationApi                    locationApi;
	@DubboReference
	TicketDiscountRecordApi        ticketDiscountRecordApi;
	@DubboReference
	UserApi                        userApi;
	@DubboReference
	EnterpriseApi                  enterpriseApi;
	@DubboReference
	OrderTicketDiscountApi         orderTicketDiscountApi;
	@DubboReference
	OrderApi                       orderApi;
	@DubboReference
	CustomerApi						customerApi;
	@DubboReference
	EmployeeApi employeeApi;


	/**
	 * 新增返利使用申请
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/addRebateUse")
	public Result<BoolObject> addRebateUse(@RequestBody @Valid AddRebateUseForm form) {
		//排除状态为审核失败和撤回
		List<UseDTO> applyList = useApi.queryUseList(ListUtil.toList(form.getApplicantId())).stream().filter(useDTO ->
				AgreementUseStatusEnum.DRAFT.getCode().equals(useDTO.getStatus()) || AgreementUseStatusEnum.SUBMIT.getCode().equals(useDTO.getStatus()) ||
						AgreementUseStatusEnum.SUCCESS.getCode().equals(useDTO.getStatus())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(applyList)) {
			return Result.failed("该条申请单已存在");
		}
		AddUseRequest useRequest = PojoUtils.map(form, AddUseRequest.class);
		useRequest.setApplicantTime(form.getCreateTime());
		useRequest.setProvinceName(form.getProvinceName());
		//保存申请
		Long eid = customerApi.getCustomerEidByEasCode(Constants.YILING_EID, useRequest.getEasCode());
		EnterpriseDTO sellerEnterprise = enterpriseApi.getByErpCode(useRequest.getSellerCode());

		if (ObjectUtil.isNull(eid) || ObjectUtil.isNull(sellerEnterprise)) {
			log.error("企业信息easCode={"+useRequest.getEasCode()+"},name={"+useRequest.getName()+"}不存在请检查:" + JSON.toJSONString(form));
			return Result.failed("企业信息easCode={"+useRequest.getEasCode()+"},name={"+useRequest.getName()+"}不存在");
		}
		useRequest.setEid(eid);
		useRequest.setSellerName(sellerEnterprise.getName());
		useRequest.setSellerEid(sellerEnterprise.getId());
		useRequest.setSellerCode(sellerEnterprise.getErpCode());
		//根据员工工号查询userId
		EnterpriseEmployeeDTO employeeDTO = employeeApi.getByCode(Constants.YILING_EID, form.getCreateUserCode());
		if (ObjectUtil.isNotNull(employeeDTO)){
			useRequest.setOpUserId(employeeDTO.getUserId());
		}else {
			log.error("冲红系统推送的返利使用单据的创建人没有找到对应的userId，请注意修数据，单据编号：{}，创建人工号：{}"
					,form.getApplicantCode(),form.getCreateUserCode());
		}
		UseDTO useDTO = useApi.saveOrUpdate(useRequest);
		//保存申请明细
		List<AddUseDetailRequest> detailRequests = ListUtil.toList();
		form.getRebateDetail().forEach(e -> {
			AddUseDetailRequest detailRequest = PojoUtils.map(e, AddUseDetailRequest.class);
			PojoUtils.map(form, detailRequest);
			detailRequest.setYear(e.getAscriptionYearValue());
			detailRequest.setMonth(e.getCycleValue());
			detailRequest.setUseId(useDTO.getId());

			detailRequests.add(detailRequest);
		});
		useDetailApi.batchSave(detailRequests);
		return Result.success(new BoolObject(Boolean.TRUE));
	}

	/**
	 * 审核返利使用申请单
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/auditRebateApply")
	public Result<BoolObject> auditRebateApply(@RequestBody @Valid AuditRebateApplyForm form) {
		List<UseDTO> useDTOS = useApi.queryUseList(ListUtil.toList(form.getApplicantId())).stream().filter(useDTO ->
				AgreementUseStatusEnum.DRAFT.getCode().equals(useDTO.getStatus()) || AgreementUseStatusEnum.SUBMIT.getCode().equals(useDTO.getStatus()) ||
						AgreementUseStatusEnum.SUCCESS.getCode().equals(useDTO.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(useDTOS)) {
			return Result.failed("申请单不存在");
		}
		UseDTO useDTO = useDTOS.get(0);
		if (AgreementUseStatusEnum.SUCCESS.getCode().equals(useDTO.getStatus()) || AgreementUseStatusEnum.WITHDRAW.getCode().equals(useDTO.getStatus())) {
			return Result.failed("当前审核单状态为" + AgreementUseStatusEnum.getByCode(useDTO.getStatus()).getName() + "，不能更在更新审核状态");
		}
		AddUseRequest addUseRequest = PojoUtils.map(form, AddUseRequest.class);
		addUseRequest.setAuditTime(form.getUpdateTime());
		addUseRequest.setId(useDTO.getId());
		addUseRequest.setOpUserId(form.getUpdateUser());
		//根据员工工号查询userId
		EnterpriseEmployeeDTO employeeDTO = employeeApi.getByCode(Constants.YILING_EID, form.getUpdateUserCode());
		if (ObjectUtil.isNotNull(employeeDTO)){
			addUseRequest.setOpUserId(employeeDTO.getUserId());
		}else {
			log.error("冲红系统审核返利使用申请单单据的更新人没有找到对应的userId，请注意修数据，单据id：{}，创建人工号：{}"
					,form.getApplicantId(),form.getUpdateUserCode());
		}
		useApi.saveOrUpdate(addUseRequest);
		//如果类型为票折则插入票折表
		if (AgreementRestitutionTypeEnum.TICKET.getCode().equals(useDTO.getExecuteMeans()) &&
				AgreementUseStatusEnum.SUCCESS.getCode().equals(form.getStatus())) {
			AddTicketDiscountRecordRequest request = new AddTicketDiscountRecordRequest();
			request.setEid(useDTO.getSellerEid());
			request.setEname(useDTO.getSellerName());
			request.setSellerErpCode(useDTO.getSellerCode());
			request.setCustomerErpCode(useDTO.getEasCode());
			request.setTicketDiscountNo(useDTO.getApplicantCode());
			request.setTotalAmount(useDTO.getTotalAmount());
			request.setAvailableAmount(useDTO.getTotalAmount());
			request.setStatus(1);
			request.setOpUserId(form.getUpdateUser());
			ticketDiscountRecordApi.saveOrUpdate(request);
		}
		return Result.success(new BoolObject(Boolean.TRUE));
	}

	/**
	 * 撤回返利使用申请单
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/withdrawRebateApply")
	public Result<BoolObject> withdrawRebateApply(@RequestBody @Valid WithdrawRebateApplyForm form) {
		List<UseDTO> useDTOS = useApi.queryUseList(ListUtil.toList(form.getApplicantId())).stream().filter(useDTO ->
				AgreementUseStatusEnum.DRAFT.getCode().equals(useDTO.getStatus()) || AgreementUseStatusEnum.SUBMIT.getCode().equals(useDTO.getStatus()) ||
						AgreementUseStatusEnum.SUCCESS.getCode().equals(useDTO.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(useDTOS)) {
			return Result.failed("申请单不存在");
		}
		UseDTO useDTO = useDTOS.get(0);
		if (!AgreementUseStatusEnum.SUCCESS.getCode().equals(useDTO.getStatus())) {
			return Result.failed("只有审核成功的申请单才能撤回，当前状态为" + AgreementUseStatusEnum.getByCode(useDTO.getStatus()).getName());
		}
		//如果撤回类型为票折
		if (AgreementRestitutionTypeEnum.TICKET.getCode().equals(useDTO.getExecuteMeans())) {
			//查询使用情况-已使用的不能撤回
			List<TicketDiscountRecordDTO> ticketDiscountRecordList = ticketDiscountRecordApi
					.getTicketDiscountRecordByTicketNoList(ListUtil.toList(useDTO.getApplicantCode())).stream().filter(e -> e.getStatus().equals(1)).collect(Collectors.toList());
			if (CollUtil.isEmpty(ticketDiscountRecordList)) {
				log.error("申请单在票折表中不存在，单号：{" + useDTO.getApplicantCode() + "}");

			} else {
				TicketDiscountRecordDTO ticketDiscountRecord = ticketDiscountRecordList.get(0);
				//更新票折表
				if (BigDecimal.ZERO.compareTo(ticketDiscountRecord.getUsedAmount()) != 0) {
					return Result.failed("该申请单已被使用，不能撤回！");
				}
				ticketDiscountRecord.setStatus(TicketDiscountStatusEnum.CLOSE.getCode());
				AddTicketDiscountRecordRequest updateRequest = PojoUtils.map(ticketDiscountRecord, AddTicketDiscountRecordRequest.class);
				updateRequest.setOpUserId(form.getUpdateUser());
				ticketDiscountRecordApi.saveOrUpdate(updateRequest);
			}
		}

		//更新申请单状态
		AddUseRequest addUseRequest = PojoUtils.map(form, AddUseRequest.class);
		addUseRequest.setStatus(AgreementUseStatusEnum.WITHDRAW.getCode());
		addUseRequest.setId(useDTO.getId());
		addUseRequest.setOpUserId(form.getUpdateUser());
		//根据员工工号查询userId
		EnterpriseEmployeeDTO employeeDTO = employeeApi.getByCode(Constants.YILING_EID, form.getUpdateUserCode());
		if (ObjectUtil.isNotNull(employeeDTO)){
			addUseRequest.setOpUserId(employeeDTO.getUserId());
		}else {
			log.error("冲红系统撤回的返利使用申请单单据的更新人没有找到对应的userId，请注意修数据，单据id：{}，创建人工号：{}"
					,form.getApplicantId(),form.getUpdateUserCode());
		}
		useApi.saveOrUpdate(addUseRequest);
		return Result.success(new BoolObject(Boolean.TRUE));
	}

	/**
	 * 查询票折使用情况
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/queryRebatePageList")
	public Result<Page<RebatePageListItemVO>> queryRebatePageList(@RequestBody @Valid QueryRebatePageListForm form) {
		//根据update时间查询使用记录
		DateTime endTime = DateUtil.beginOfDay(new Date());
		DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), form.getDayNum() * (-1)));
		//分页查询使用情况
		QueryTicketDiscountPageListRequest request = PojoUtils.map(form, QueryTicketDiscountPageListRequest.class);
		request.setStartTime(startTime);
		request.setEndTime(endTime);
		Page<TicketDiscountRecordDTO> page = ticketDiscountRecordApi.queryPageListByUseTime(request);
		Page<RebatePageListItemVO> result = PojoUtils.map(page, RebatePageListItemVO.class);

		if (CollUtil.isNotEmpty(result.getRecords())) {
			List<String> ticketNoList = result.getRecords().stream().map(RebatePageListItemVO::getTicketDiscountNo).collect(Collectors.toList());
			//查询票折下的订单
			List<OrderTicketDiscountDTO> orderTicketList = orderTicketDiscountApi.getOrderTicketDiscountByListNos(ticketNoList);
			List<OrderTicketDiscountVO> orderTicketDiscountVOS = PojoUtils.map(orderTicketList, OrderTicketDiscountVO.class);
			//票折下的订单map
			Map<String, List<OrderTicketDiscountVO>> ticketOrderMap = orderTicketDiscountVOS.stream()
					.collect(Collectors.groupingBy(OrderTicketDiscountVO::getTicketDiscountNo));
			if (CollUtil.isNotEmpty(ticketOrderMap)) {
				//查询订单下的发货单
				List<OrderTicketDiscountVO> orderList = orderTicketDiscountVOS.stream().distinct().collect(Collectors.toList());
				Map<Long, OrderDTO> orderDTOMap = orderApi.listByIds(orderList.stream().map(OrderTicketDiscountVO::getOrderId)
						.collect(Collectors.toList())).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
				//为票折下的订单赋值出库单号等xinxi
				ticketOrderMap.forEach((ticketNo, e) -> {
					for (int i = 0; i < e.size(); i++) {
						OrderTicketDiscountVO orderTicketBO = e.get(i);
						OrderDTO orderDTO = orderDTOMap.get(orderTicketBO.getOrderId());
						if (ObjectUtil.isNotNull(orderDTO)) {
							PojoUtils.map(orderDTO, orderTicketBO);
						}
					}
				});
			}
			List<RebatePageListItemVO> records = result.getRecords();
			//查询用户名
			Map<Long, String> userMap = userApi.listByIds(records.stream().map(RebatePageListItemVO::getUpdateUser)
					.collect(Collectors.toList())).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
			records.forEach(e -> {
				e.setUpdateUserName(userMap.getOrDefault(e.getUpdateUser(), ""));
				//补全票折使用明细
				List<OrderTicketDiscountVO> orderTicketBOList = ticketOrderMap.get(e.getTicketDiscountNo());
				if (CollUtil.isNotEmpty(orderTicketBOList)) {
					List<RebatePageListItemVO.Order> detail = PojoUtils.map(orderTicketBOList, RebatePageListItemVO.Order.class);
					e.setDetail(detail);
				}
			});
		}
		return Result.success(result);
	}


}
