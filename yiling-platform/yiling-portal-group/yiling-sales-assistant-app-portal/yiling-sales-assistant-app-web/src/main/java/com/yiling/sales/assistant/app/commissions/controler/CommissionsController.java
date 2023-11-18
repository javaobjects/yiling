package com.yiling.sales.assistant.app.commissions.controler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.sales.assistant.app.commissions.form.QueryCommissionsDetailPageListForm;
import com.yiling.sales.assistant.app.commissions.form.QueryCommissionsPageListForm;
import com.yiling.sales.assistant.app.commissions.vo.CommissionsDetailPageListItemVO;
import com.yiling.sales.assistant.app.commissions.vo.CommissionsDetailPageVO;
import com.yiling.sales.assistant.app.commissions.vo.CommissionsPageListItemVO;
import com.yiling.sales.assistant.app.commissions.vo.CommissionsPageVO;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.api.CommissionsDetailApi;
import com.yiling.sales.assistant.commissions.api.CommissionsUserApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserStatisticsDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dexi.yao
 * @date 2021-09-17
 */
@RestController
@Api(tags ="佣金相关接口")
@RequestMapping("/commissions")
public class CommissionsController {

	@DubboReference
	CommissionsUserApi   commissionsUserApi;
	@DubboReference
	CommissionsApi commissionsApi;
	@DubboReference
	UserApi        userApi;
	@DubboReference
	OrderApi       orderApi;
	@DubboReference
	CommissionsDetailApi commissionsDetailApi;
	@DubboReference
    EnterpriseApi enterpriseApi;


	/**
	 * 查询用户佣金明细
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation("查询用户佣金明细")
	@PostMapping("/queryCommissionsPageList")
	public Result<CommissionsPageVO<CommissionsPageListItemVO>> queryCommissionsPageList(@Valid @RequestBody QueryCommissionsPageListForm form,@CurrentUser CurrentStaffInfo staffInfo) {
		CommissionsPageVO<CommissionsPageListItemVO> result;
		UserDTO useDTO = userApi.getById(staffInfo.getCurrentUserId());
		if (ObjectUtil.isNull(useDTO)) {
			return Result.failed("用户不存在");
		}
		//查询收益统计
		CommissionsUserStatisticsDTO userStatisticsDTO = commissionsApi.commissionsUserStatistics(staffInfo.getCurrentUserId());
		result = PojoUtils.map(userStatisticsDTO, CommissionsPageVO.class);
		QueryCommissionsPageListRequest request = PojoUtils.map(form,QueryCommissionsPageListRequest.class);
		request.setEffectStatusEnum(EffectStatusEnum.VALID);
		request.setUserIdList(ListUtil.toList(staffInfo.getCurrentUserId()));
		//查询收支明细
		Page<CommissionsDTO> page = commissionsApi.queryCommissionsPageList(request);
		List<CommissionsDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}

		List<CommissionsPageListItemVO> items = PojoUtils.map(records, CommissionsPageListItemVO.class);
		result.setRecords(items);
		PojoUtils.map(page, result);

		return Result.success(result);
	}

	/**
	 * 查询佣金记录的明细
	 *
	 * @return
	 */
	@ApiOperation("查询佣金记录的明细")
	@PostMapping("/queryCommissionsDetailPageList")
	public Result<CommissionsDetailPageVO<CommissionsDetailPageListItemVO>> queryCommissionsDetailPageList(
			@Valid @RequestBody QueryCommissionsDetailPageListForm form) {
		CommissionsDetailPageVO<CommissionsDetailPageListItemVO> result;

		CommissionsDTO commissionsDTO = commissionsApi.queryById(form.getCommissionsId());
		if (ObjectUtil.isNull(commissionsDTO)) {
			return Result.failed("佣金记录不存在");
		}
		result = PojoUtils.map(commissionsDTO, CommissionsDetailPageVO.class);
		QueryCommissionsDetailPageListRequest request = PojoUtils.map(form, QueryCommissionsDetailPageListRequest.class);
		Page<CommissionsDetailDTO> page = commissionsDetailApi.queryCommissionsDetailPageList(request);
		List<CommissionsDetailDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
		List<CommissionsDetailPageListItemVO> items = PojoUtils.map(records, CommissionsDetailPageListItemVO.class);
		//查询订单信息
		if (ObjectUtil.equal(commissionsDTO.getFinishType(), FinishTypeEnum.AMOUNT.getCode())||ObjectUtil.equal(commissionsDTO.getFinishType(), FinishTypeEnum.MONEY.getCode())){
            //查询订单信息
            Map<Long, OrderDTO> orderDTOMap = orderApi.listByIds(items.stream().distinct().map(CommissionsDetailPageListItemVO::getOrderId)
                    .collect(Collectors.toList())).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
            items.forEach(e -> {
                OrderDTO orderDTO = orderDTOMap.get(e.getOrderId());
                if (ObjectUtil.isNotNull(orderDTO)) {
                    e.setBuyerEname(orderDTO.getBuyerEname());
                    e.setCreateTime(orderDTO.getCreateTime());
                }
            });
        }
		//查询拉新企业负责人
		if (ObjectUtil.equal(commissionsDTO.getFinishType(), FinishTypeEnum.NEW_ENT.getCode())){
            //查询企业信息
            Map<Long, EnterpriseDTO> entDTOMap = enterpriseApi.listByIds(items.stream().distinct().map(CommissionsDetailPageListItemVO::getNewEntId)
                    .collect(Collectors.toList())).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
            items.forEach(e -> {
                EnterpriseDTO enterpriseDTO = entDTOMap.get(e.getNewEntId());
                if (ObjectUtil.isNotNull(enterpriseDTO)) {
                    e.setContactor(enterpriseDTO.getContactor());
                }
            });
        }
		//查询拉人的人员电话
		if (ObjectUtil.equal(commissionsDTO.getFinishType(), FinishTypeEnum.NEW_USER.getCode())){
            //查询企业信息
            Map<Long, UserDTO> userDTOMap = userApi.listByIds(items.stream().distinct().map(CommissionsDetailPageListItemVO::getNewUserId)
                    .collect(Collectors.toList())).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
            items.forEach(e -> {
                UserDTO userDTO = userDTOMap.get(e.getNewUserId());
                if (ObjectUtil.isNotNull(userDTO)) {
                    e.setNewUserMobile(userDTO.getMobile());
                    e.setNewUserName(userDTO.getName());
                }
            });
        }
        PojoUtils.map(page, result);
		result.setRecords(items);
		return Result.success(result);
	}
}
