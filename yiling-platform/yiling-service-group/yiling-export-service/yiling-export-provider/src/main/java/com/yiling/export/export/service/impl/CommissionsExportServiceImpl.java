package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportCommissionsDetailBO;
import com.yiling.export.export.bo.ExportCommissionsUserBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.api.CommissionsDetailApi;
import com.yiling.sales.assistant.commissions.api.CommissionsUserApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.BatchQueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.ExportCommissionsRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsDetailStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Service("commissionsExportService")
@Slf4j
public class CommissionsExportServiceImpl implements BaseExportQueryDataService<ExportCommissionsRequest> {

	@DubboReference
	CommissionsUserApi   commissionsUserApi;
	@DubboReference
	CommissionsApi       commissionsApi;
	@DubboReference
	CommissionsDetailApi commissionsDetailApi;
	@DubboReference
	UserApi              userApi;
    @DubboReference
    StaffApi staffApi;
	@DubboReference
	TaskApi              taskApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

	private static final LinkedHashMap<String, String> FIELD_SHEET  = new LinkedHashMap<String, String>() {

		{
			put("userName", "获佣人");
//			put("totalAmount", "累计佣金总额");
//			put("paidAmount", "已结算总金额");
			put("surplusAmount", "待结算总额");
			put("mobile", "联系方式");
		}
	};
	private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

		{
			put("id", "佣金明细ID");
			put("taskId", "任务ID");
			put("taskName", "任务名称");
			put("orderCode", "订单编号/单据编号");
			put("newEntId", "邀户ID");
			put("newUserId", "邀人ID");
			put("startTime", "开始时间");
			put("endTime", "结束时间");
			put("finishTypeName", "任务类型");
			put("subAmount", "待发放金额");
			put("subordinateUserName", "下线推广人");
			put("userName", "获佣人");
            put("ownershipName", "所在企业（获佣人）");
            put("userType", "任务承接人群（获佣人）");
            put("mobile", "联系方式（获佣人）");
		}
	};

	@Override
	public QueryExportDataDTO queryData(ExportCommissionsRequest request) {
		//需要返回的对象
		QueryExportDataDTO result = new QueryExportDataDTO();

		//不同sheet数据
		List<Map<String, Object>> userCommissionsData = new ArrayList<>();
		List<Map<String, Object>> commissionsDetailData = new ArrayList<>();
        QueryCommissionsUserPageListRequest pageListRequest=new QueryCommissionsUserPageListRequest();

        //查询用户
        List<Long> idList= ListUtil.toList();
        if (StrUtil.isNotEmpty(request.getUsername())){
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(request.getUsername());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            idList = staffList.stream().map(Staff::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(idList)){
                idList=ListUtil.toList(-1L);
            }
        }
        if (StrUtil.isNotEmpty(request.getMobile())){
            Staff staff = staffApi.getByMobile(request.getMobile());
           if (ObjectUtil.isNotNull(staff)){
              if (CollUtil.isEmpty(idList)){
                  idList.add(staff.getId());
              }else {
                  if (ObjectUtil.notEqual(idList.stream().findAny().orElse(null),staff.getId())){
                      idList=ListUtil.toList(-1L);
                  }
              }
           }else {
               idList=ListUtil.toList(-1L);
           }
        }
        pageListRequest.setUserIdList(idList);


		int mainCurrent = 1;
		Page<CommissionsUserDTO> mainPage;
		//分页查询申请单列表
		do {

            pageListRequest.setCurrent(mainCurrent);
            pageListRequest.setSize(50);
			mainPage = commissionsUserApi.queryCommissionsUserPageList(pageListRequest);
			List<CommissionsUserDTO> userRecords = mainPage.getRecords().stream().filter(e -> e.getTotalAmount() != null).collect(Collectors.toList());
			List<Long> userIdList = userRecords.stream().map(CommissionsUserDTO::getUserId).distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(userIdList)) {
				break;
			}
			//查询用户名
			Map<Long, UserDTO> userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, e->e));

			List<ExportCommissionsUserBO> userList = PojoUtils.map(userRecords, ExportCommissionsUserBO.class);
			userList.forEach(e -> {
                UserDTO userDTO = userMap.get(e.getUserId());
                if (ObjectUtil.isNotNull(userDTO)){
                    e.setUserName(userDTO.getName());
                    e.setMobile(userDTO.getMobile());
                }
				e.setTotalAmount(e.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				e.setPaidAmount(e.getPaidAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				e.setSurplusAmount(e.getSurplusAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				Map<String, Object> userDataExportPojo = BeanUtil.beanToMap(e);
				userCommissionsData.add(userDataExportPojo);
			});
			//查询用户下的佣金明细
			QueryCommissionsPageListRequest commissionsRequest = new QueryCommissionsPageListRequest();
			commissionsRequest.setUserIdList(userIdList);
			commissionsRequest.setEffectStatusEnum(EffectStatusEnum.VALID);
			commissionsRequest.setTypeEnum(CommissionsTypeEnum.INPUT);
			commissionsRequest.setStatusEnum(CommissionsStatusEnum.UN_SETTLEMENT);
			int commissionsCurrent = 1;
			Page<CommissionsDTO> commissionsPage;
			do {
				//分页查询未结清的佣金记录
				commissionsRequest.setCurrent(commissionsCurrent);
				commissionsRequest.setSize(50);
				commissionsPage = commissionsApi.queryCommissionsPageList(commissionsRequest);
				//未结清的佣金记录map
				Map<Long, CommissionsDTO> commissionsMap = commissionsPage.getRecords().stream().collect(Collectors.toMap(CommissionsDTO::getId, e -> e));
				//查询marketTask
				Map<Long, TaskDTO> taskDTOMap = taskApi.queryMarketTaskListById(commissionsPage.getRecords().stream().map(CommissionsDTO::getTaskId)
						.distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(TaskDTO::getId, e -> e));

				//分页查询未结清的佣金记录下的未兑付的佣金明细
				BatchQueryCommissionsDetailPageListRequest commissionsDetailRequest = new BatchQueryCommissionsDetailPageListRequest();
				commissionsDetailRequest.setCommissionsIdList(new ArrayList<>(commissionsMap.keySet()));
				commissionsDetailRequest.setStatusEnum(CommissionsDetailStatusEnum.UN_SETTLEMENT);

				int commissionsDetailCurrent = 1;
				Page<CommissionsDetailDTO> commissionsDetailPage;
				do {
					commissionsDetailRequest.setCurrent(commissionsDetailCurrent);
					commissionsDetailRequest.setSize(50);
					commissionsDetailPage = commissionsDetailApi.batchQueryCommissionsDetailPageList(commissionsDetailRequest);

					List<CommissionsDetailDTO> detailPageRecords = commissionsDetailPage.getRecords();
					if (CollUtil.isNotEmpty(detailPageRecords)) {
						List<ExportCommissionsDetailBO> detailBOS = PojoUtils.map(detailPageRecords, ExportCommissionsDetailBO.class);

                        //查询用户信息
                        Map<Long, String> subordinateUserMap= MapUtil.newHashMap();
                        List<Long> subordinateUserIdList = commissionsPage.getRecords().stream().map(CommissionsDTO::getSubordinateUserId).filter(e->ObjectUtil.notEqual(e,0L)).distinct().collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(subordinateUserIdList)){
                            subordinateUserMap = userApi.listByIds(subordinateUserIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
                        }
                        //查询所属信息
                        Map<Long, String> ownershipEntMap= MapUtil.newHashMap();
                        List<Long> ownershipEidList =  commissionsPage.getRecords().stream().map(CommissionsDTO::getOwnershipEid).filter(e->ObjectUtil.notEqual(e,0L)).distinct().collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(ownershipEidList)){
                            ownershipEntMap = enterpriseApi.listByIds(ownershipEidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
                        }

						//组装数据
                        Map<Long, String> finalSubordinateUserMap = subordinateUserMap;
                        Map<Long, String> finalOwnershipEntMap = ownershipEntMap;
                        detailBOS.forEach(e -> {
							e.setSubAmount(e.getSubAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
							CommissionsDTO commissionsDTO = commissionsMap.get(e.getCommissionsId());
                            //设置获佣人所属企业相关
                            //获佣人所属企业
                            e.setOwnershipName(finalOwnershipEntMap.getOrDefault(commissionsDTO.getOwnershipEid(),""));
                            //下线名子
                            if (ObjectUtil.notEqual(commissionsDTO.getSubordinateUserId(),0L)){
                                e.setSubordinateUserName(finalSubordinateUserMap.getOrDefault(commissionsDTO.getSubordinateUserId(),""));
                            }
                            //获佣人所属人群
                            if (ObjectUtil.notEqual(commissionsDTO.getUserType(),0)){
                                e.setUserType(UserTypeEnum.getByCode(commissionsDTO.getUserType()).getName());
                            }


							if(ObjectUtil.equal(e.getNewEntId(),0L)||ObjectUtil.isNull(e.getNewEntId())){
							    e.setNewEntId(null);
                            }
							if(ObjectUtil.equal(e.getNewUserId(),0L)||ObjectUtil.isNull(e.getNewUserId())){
							    e.setNewUserId(null);
                            }
							if(ObjectUtil.equal(commissionsDTO.getSources(), CommissionsSourcesEnum.SUBORDINATE.getCode())){
							    e.setNewUserId(null);
                            }
							if (ObjectUtil.isNotNull(commissionsDTO)) {
								e.setTaskName(commissionsDTO.getTaskName());
								e.setFinishTypeName(FinishTypeEnum.getByCode(commissionsDTO.getFinishType()));
                                UserDTO userDTO = userMap.get(commissionsDTO.getUserId());
                                if (ObjectUtil.isNotNull(userDTO)){
                                    e.setUserName(userDTO.getName());
                                    e.setMobile(userDTO.getMobile());
                                }
							}
							TaskDTO taskDTO = taskDTOMap.get(e.getTaskId());
							if (ObjectUtil.isNotNull(taskDTO)) {
								e.setStartTime(DateUtil.format(taskDTO.getStartTime(), "yyyy-MM-dd"));
								e.setEndTime(DateUtil.format(taskDTO.getEndTime(), "yyyy-MM-dd"));
							}
							Map<String, Object> commissionsDetailDataExportPojo = BeanUtil.beanToMap(e);
							commissionsDetailData.add(commissionsDetailDataExportPojo);
						});
					}
					commissionsDetailCurrent = commissionsDetailCurrent + 1;
				} while (commissionsDetailPage != null && CollectionUtils.isNotEmpty(commissionsDetailPage.getRecords()));
				commissionsCurrent = commissionsCurrent + 1;
			} while (commissionsPage != null && CollectionUtils.isNotEmpty(commissionsPage.getRecords()));
			mainCurrent = mainCurrent + 1;
		} while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

		//用户佣金
		ExportDataDTO exportUserCommissionsDTO = new ExportDataDTO();
		exportUserCommissionsDTO.setSheetName("sheet1");
		// 页签字段
		exportUserCommissionsDTO.setFieldMap(FIELD_SHEET);
		// 页签数据
		exportUserCommissionsDTO.setData(userCommissionsData);


		//用户佣金明细明细
		ExportDataDTO exportCommissionsDetailDTO = new ExportDataDTO();
		exportCommissionsDetailDTO.setSheetName("sheet2");
		// 页签字段
		exportCommissionsDetailDTO.setFieldMap(FIELD_SHEET2);
		// 页签数据
		exportCommissionsDetailDTO.setData(commissionsDetailData);
		//封装excel
		List<ExportDataDTO> sheets = new ArrayList<>();
		sheets.add(exportUserCommissionsDTO);
		sheets.add(exportCommissionsDetailDTO);
		result.setSheets(sheets);

		return result;
	}

	@Override
	public ExportCommissionsRequest getParam(Map<String, Object> map) {
		return PojoUtils.map(map, ExportCommissionsRequest.class);
	}
}
