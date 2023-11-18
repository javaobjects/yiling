package com.yiling.admin.sales.assistant.commissions.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.banner.form.ImportCommissionsForm;
import com.yiling.admin.sales.assistant.commissions.form.QueryCommissionsDetailPageListForm;
import com.yiling.admin.sales.assistant.commissions.form.QueryCommissionsPageListForm;
import com.yiling.admin.sales.assistant.commissions.form.QueryTaskPageListForm;
import com.yiling.admin.sales.assistant.commissions.handler.ImportCommissionsHandler;
import com.yiling.admin.sales.assistant.commissions.vo.CommissionsDetailItemPageListVO;
import com.yiling.admin.sales.assistant.commissions.vo.CommissionsDetailPageVO;
import com.yiling.admin.sales.assistant.commissions.vo.CommissionsPageListItemVO;
import com.yiling.admin.sales.assistant.commissions.vo.CommissionsPageVO;
import com.yiling.admin.sales.assistant.commissions.vo.TaskPageListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.api.CommissionsDetailApi;
import com.yiling.sales.assistant.commissions.api.CommissionsUserApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-09-23
 */
@RestController
@Api(tags = "佣金相关接口")
@RequestMapping("/commissions")
@Slf4j
public class CommissionsController {

	@DubboReference
	CommissionsUserApi   commissionsUserApi;
	@DubboReference
	CommissionsApi       commissionsApi;
	@DubboReference
	UserApi              userApi;
    @DubboReference
    StaffApi staffApi;
	@DubboReference
	CommissionsDetailApi commissionsDetailApi;
	@DubboReference
	TaskApi              taskApi;
	@DubboReference
    EnterpriseApi enterpriseApi;

	@Autowired
	ImportCommissionsHandler importCommissionsHandler;

	/**
	 * 查询用户佣金明细
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation("查询用户佣金列表")
	@PostMapping("/queryCommissionsPageList")
	public Result<CommissionsPageVO<CommissionsPageListItemVO>> queryCommissionsPageList(@Valid @RequestBody QueryCommissionsPageListForm form) {
		CommissionsPageVO<CommissionsPageListItemVO> result;
		//查询佣金统计
		StatisticsCommissionsUserDTO statisticsCommissionsUserDTO = commissionsUserApi.statisticsCommissionsUser();
		if (ObjectUtil.isNull(statisticsCommissionsUserDTO)){
		    return Result.success(new CommissionsPageVO<>());
        }
		result = PojoUtils.map(statisticsCommissionsUserDTO, CommissionsPageVO.class);
		//查询用户
        List<Long> idList=ListUtil.toList();
        if (StrUtil.isNotEmpty(form.getUsername())){
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(form.getUsername());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            idList = staffList.stream().map(Staff::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(idList)){
                return Result.success(new CommissionsPageVO<>());
            }
        }
        if (StrUtil.isNotEmpty(form.getMobile())){
            Staff staff = staffApi.getByMobile(form.getMobile());
            if (ObjectUtil.isNull(staff)){
                return Result.success(new CommissionsPageVO<>());
            }
            if(CollUtil.isNotEmpty(idList)&&!idList.contains(staff.getId())){
                return Result.success(new CommissionsPageVO<>());
            }
            idList.add(staff.getId());
        }
		QueryCommissionsUserPageListRequest request = PojoUtils.map(form,QueryCommissionsUserPageListRequest.class);
		request.setUserIdList(idList);
		//查询收支明细
		Page<CommissionsUserDTO> page = commissionsUserApi.queryCommissionsUserPageList(request);
		List<CommissionsUserDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
        //查询用户名
        Map<Long, UserDTO> userDTOMap = userApi.listByIds(records.stream().map(CommissionsUserDTO::getUserId).distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(UserDTO::getId,e->e));
        List<CommissionsPageListItemVO> items = PojoUtils.map(records, CommissionsPageListItemVO.class);
		result.setRecords(items);
		result.getRecords().forEach(itemVO -> {
            UserDTO userDTO = userDTOMap.get(itemVO.getUserId());
            if(ObjectUtil.isNotNull(userDTO)){
                itemVO.setName(userDTO.getName());
                itemVO.setMobile(userDTO.getMobile());
            }
        });
		PojoUtils.map(page, result);

		return Result.success(result);
	}

	/**
	 * 查询用户任务列表
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation("查询用户任务列表")
	@PostMapping("/queryTaskPageList")
	public Result<Page<TaskPageListItemVO>> queryTaskPageList(@Valid @RequestBody QueryTaskPageListForm form) {
		QueryCommissionsPageListRequest request = PojoUtils.map(form,QueryCommissionsPageListRequest.class);
//		request.setTypeEnum(CommissionsTypeEnum.INPUT);
		request.setEffectStatusEnum(EffectStatusEnum.VALID);
		request.setUserIdList(ListUtil.toList(form.getUserId()));
		Page<CommissionsDTO> page = commissionsApi.queryCommissionsPageList(request);
		List<CommissionsDTO> records = page.getRecords();

		if (CollUtil.isEmpty(records)) {
			return Result.success(PojoUtils.map(page, TaskPageListItemVO.class));
		}
		Map<Long, TaskDTO> taskDTOMap = taskApi.queryMarketTaskListById(records.stream().map(CommissionsDTO::getTaskId)
				.distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(TaskDTO::getId, e -> e));
		Page<TaskPageListItemVO> result = PojoUtils.map(page, TaskPageListItemVO.class);
		result.getRecords().forEach(e -> {
			TaskDTO taskDTO = taskDTOMap.get(e.getTaskId());
			if (ObjectUtil.isNotNull(taskDTO)) {
				e.setStartTime(taskDTO.getStartTime());
				e.setEndTime(taskDTO.getEndTime());
			}
		});
		return Result.success(result);
	}

	/**
	 * 查询用户佣金详情
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation("查询用户佣金详情")
	@PostMapping("/queryCommissionsDetailPageList")
	public Result<CommissionsDetailPageVO<CommissionsDetailItemPageListVO>> queryCommissionsDetailPageList(@Valid @RequestBody QueryCommissionsDetailPageListForm form) {
		CommissionsDetailPageVO<CommissionsDetailItemPageListVO> result;

		CommissionsDTO commissionsDTO = commissionsApi.queryById(form.getCommissionsId());

		if (ObjectUtil.isNull(commissionsDTO)) {
			return Result.failed("佣金记录不存在");
		}
		result = PojoUtils.map(commissionsDTO, CommissionsDetailPageVO.class);
        //查询联系方式
        UserDTO user = userApi.getById(commissionsDTO.getUserId());
        result.setMobile(user.getMobile());
        //查询任务起止时间
		Map<Long, TaskDTO> taskDTOMap = taskApi.queryMarketTaskListById(ListUtil.toList(commissionsDTO.getTaskId())).stream()
				.collect(Collectors.toMap(TaskDTO::getId, e -> e));
		TaskDTO taskDTO = taskDTOMap.get(commissionsDTO.getTaskId());
		if (ObjectUtil.isNotNull(taskDTO)) {
			result.setStartTime(taskDTO.getStartTime());
			result.setEndTime(taskDTO.getEndTime());
		}
		//查询任务计算方式
		String ruleValue = taskApi.getRuleValue(commissionsDTO.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.COMPUTE_MODE.toString());
		result.setRuleValue(StrUtil.isEmpty(ruleValue)==Boolean.TRUE? "多品计算":ruleValue);

		//查询佣金明细
		QueryCommissionsDetailPageListRequest request = PojoUtils.map(form, QueryCommissionsDetailPageListRequest.class);
		Page<CommissionsDetailDTO> page = commissionsDetailApi.queryCommissionsDetailPageList(request);
		List<CommissionsDetailDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
		List<CommissionsDetailItemPageListVO> vos = PojoUtils.map(records, CommissionsDetailItemPageListVO.class);
		//查询企业信息
        List<Long> eidList = vos.stream().map(CommissionsDetailItemPageListVO::getNewEntId).distinct().collect(Collectors.toList());
        if (ObjectUtil.notEqual(commissionsDTO.getTaskOwnershipEid(),0L)){
            eidList.add(commissionsDTO.getTaskOwnershipEid());
        }
        eidList=eidList.stream().distinct().collect(Collectors.toList());
        Map<Long, String> entMap= MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(eidList)){
            entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        }
        //查询用户信息
        Map<Long, String> userMap= MapUtil.newHashMap();
        List<Long> newUserList = vos.stream().map(CommissionsDetailItemPageListVO::getNewUserId).collect(Collectors.toList());
        if (ObjectUtil.notEqual(commissionsDTO.getSubordinateUserId(),0L)){
            newUserList.add(commissionsDTO.getSubordinateUserId());
        }
        newUserList=newUserList.stream().distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(newUserList)){
            userMap = userApi.listByIds(newUserList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        Map<Long, String> finalEntMap = entMap;
        Map<Long, String> finalUserMap = userMap;
        vos.forEach(detailItemPageListVO -> {
            //设置任务的所属企业等信息
            detailItemPageListVO.setTaskOwnershipName(finalEntMap.getOrDefault(commissionsDTO.getTaskOwnershipEid(),""));
            detailItemPageListVO.setTaskUserType(commissionsDTO.getTaskUserType());
            if (ObjectUtil.notEqual(commissionsDTO.getSubordinateUserId(),0L)){
                detailItemPageListVO.setSubordinateUserName(finalUserMap.getOrDefault(commissionsDTO.getSubordinateUserId(),""));
            }

            if (ObjectUtil.notEqual(detailItemPageListVO.getNewEntId(),0L)&&ObjectUtil.equal(detailItemPageListVO.getOrderId(),0L)){
                detailItemPageListVO.setName(finalEntMap.getOrDefault(detailItemPageListVO.getNewEntId(),""));
                return;
            }
            if (ObjectUtil.notEqual(detailItemPageListVO.getNewUserId(),0L)&&ObjectUtil.equal(detailItemPageListVO.getOrderId(),0L)){
                detailItemPageListVO.setName(finalUserMap.getOrDefault(detailItemPageListVO.getNewUserId(),""));
                return;
            }
            if (ObjectUtil.isNotEmpty(detailItemPageListVO.getBuyMemberName())){
                detailItemPageListVO.setName(detailItemPageListVO.getBuyMemberName());
            }
        });
		PojoUtils.map(page, result);
		result.setRecords(vos);

		return Result.success(result);
	}

	@ApiOperation(value = "导入佣金兑付")
	@PostMapping("/importCommissions")
	public Result<ImportResultVO> importCommissions(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
		ImportParams params = new ImportParams();
		params.setNeedSave(false);
		params.setNeedVerify(true);
		params.setVerifyHandler(importCommissionsHandler);

		InputStream in;
		try {
			in = file.getInputStream();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
		}

		ImportResultModel importResultModel;
		try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,adminInfo.getCurrentUserId());
			importResultModel = ExeclImportUtils.importExcelMore(in, ImportCommissionsForm.class, params, importCommissionsHandler, paramMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
		}

		return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
	}
}
