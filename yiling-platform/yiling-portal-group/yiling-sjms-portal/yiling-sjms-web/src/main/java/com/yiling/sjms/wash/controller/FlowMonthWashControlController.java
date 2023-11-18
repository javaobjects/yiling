package com.yiling.sjms.wash.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.api.ErpClientWashPlanApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.sjms.wash.api.SjmsFlowMonthWashControlApi;
import com.yiling.sjms.wash.form.QueryFlowMonthWashControlPageForm;
import com.yiling.sjms.wash.form.SaveOrUpdateFlowMonthWashControlForm;
import com.yiling.sjms.wash.form.UpdateFlowMonthWashControlStatusForm;
import com.yiling.sjms.wash.vo.FlowMonthWashControlVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 流向日程表 前端控制器
 * </p>
 *
 * @author baifc
 * @since 2023-03-03
 */
@Slf4j
@RestController
@RequestMapping("/flowMonthWashControl")
@Api(tags = "月流向清洗日程表")
public class FlowMonthWashControlController extends BaseController {

    @DubboReference
    private FlowMonthWashControlApi     flowMonthWashControlApi;
    @DubboReference
    private ErpClientWashPlanApi        erpClientWashPlanApi;
    @DubboReference
    private FlowMonthWashTaskApi        flowMonthWashTaskApi;
    @DubboReference
    private ErpClientApi                erpClientApi;
    @DubboReference
    private FlowPurchaseApi             flowPurchaseApi;
    @DubboReference
    private FlowSaleApi                 flowSaleApi;
    @DubboReference
    private FlowGoodsBatchDetailApi     flowGoodsBatchDetailApi;
    @DubboReference(timeout = 1000 * 60)
    private CrmEnterpriseApi            crmEnterpriseApi;
    @DubboReference(timeout = 1000 * 60)
    private AgencyBackupApi             agencyBackupApi;
    @DubboReference(async = true)
    private SjmsFlowMonthWashControlApi sjmsFlowMonthWashControlApi;

    @ApiOperation(value = "月流向清洗日程列表")
    @PostMapping("/list")
    public Result<Page<FlowMonthWashControlVO>> flowMonthWashControlListPage(@RequestBody @Valid QueryFlowMonthWashControlPageForm form) {
        QueryFlowMonthWashControlPageRequest request = PojoUtils.map(form, QueryFlowMonthWashControlPageRequest.class);
        Page<FlowMonthWashControlVO> pageResult = PojoUtils.map(flowMonthWashControlApi.listPage(request), FlowMonthWashControlVO.class);
        return Result.success(pageResult);
    }

    @ApiOperation(value = "添加修改月流向清洗日程")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveOrUpdateFlowMonthWashControlForm form) {
        if (form.getId() != null && form.getId() != 0) {
            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getById(form.getId());
            if (flowMonthWashControlDTO.getTaskStatus() == 1) {
                return Result.failed("日程已经关闭不能编辑");
            }
        } else {
            boolean bool = flowMonthWashControlApi.verifyStatus();
            if (bool) {
                return Result.failed("请先关闭之前的日程，再进行新增！");
            }
        }
        if (form.getDataStartTime().getTime() >= form.getDataEndTime().getTime()) {
            return Result.failed("时间输入错误");
        }

        if (form.getGoodsMappingStartTime().getTime() >= form.getGoodsMappingEndTime().getTime()) {
            return Result.failed("时间输入错误");
        }
        if (form.getCustomerMappingStartTime().getTime() >= form.getCustomerMappingEndTime().getTime()) {
            return Result.failed("时间输入错误");
        }
        if (form.getGoodsBatchStartTime().getTime() >= form.getGoodsBatchEndTime().getTime()) {
            return Result.failed("时间输入错误");
        }
        if (form.getFlowCrossStartTime().getTime() >= form.getFlowCrossEndTime().getTime()) {
            return Result.failed("时间输入错误");
        }

        SaveOrUpdateFlowMonthWashControlRequest request = PojoUtils.map(form, SaveOrUpdateFlowMonthWashControlRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(flowMonthWashControlApi.saveOrUpdate(request));
    }

    @ApiOperation(value = "新增日程校验是否可以新增")
    @PostMapping("/verifySave")
    public Result<Boolean> verifySave() {
        boolean bool = flowMonthWashControlApi.verifyStatus();
        if (bool) {
            return Result.failed("请先关闭之前的日程，再进行新增！");
        }
        return Result.success(true);
    }

    @ApiOperation(value = "获取月流向清洗日程")
    @GetMapping("/get")
    public Result<FlowMonthWashControlVO> get(@RequestParam @ApiParam(value = "id", required = true) Long id) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getById(id);
        return Result.success(PojoUtils.map(flowMonthWashControlDTO, FlowMonthWashControlVO.class));
    }

    @ApiOperation(value = "修改每一个阶段状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateFlowMonthWashControlStatusForm form) {
        //验证是否可以备份数据
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getById(form.getId());
        if (flowMonthWashControlDTO == null) {
            return Result.failed("没有找到对应的日程");
        }
        SaveOrUpdateFlowMonthWashControlRequest request  = new SaveOrUpdateFlowMonthWashControlRequest();
        request.setId(form.getId());
        if(form.getStage()==1){
            if(form.getEmployeeBackupStatus()==2) {
                AgencyBackRequest agencyBackRequest = new AgencyBackRequest();
                StringBuffer sb = new StringBuffer("");
                sb.append(flowMonthWashControlDTO.getYear()).append(String.format("%02d", flowMonthWashControlDTO.getMonth()));
                agencyBackRequest.setYearMonthFormat(Integer.parseInt(sb.toString()));
                agencyBackRequest.setOpUserId(userInfo.getCurrentUserId());
                agencyBackupApi.esbInfoBackup(agencyBackRequest);
                request.setEmployeeBackupTime(new Date());
            }
            request.setEmployeeBackupStatus(form.getEmployeeBackupStatus());
        }else if(form.getStage()==2){
            if(form.getBasisStatus()==3){
                request.setBasisTime(new Date());
            }
            request.setBasisStatus(form.getBasisStatus());
        }else if(form.getStage()==3){
            if(form.getBasisBackupStatus()==2) {
                AgencyBackRequest agencyBackRequest = new AgencyBackRequest();
                StringBuffer sb = new StringBuffer("");
                sb.append(flowMonthWashControlDTO.getYear()).append(String.format("%02d", flowMonthWashControlDTO.getMonth()));
                agencyBackRequest.setYearMonthFormat(Integer.parseInt(sb.toString()));
                agencyBackRequest.setOpUserId(userInfo.getCurrentUserId());
                agencyBackupApi.agencyInfoBackup(agencyBackRequest);
                request.setBasisBackupTime(new Date());
            }
            request.setBasisBackupStatus(form.getBasisBackupStatus());
        }else if(form.getStage()==4){
            //先生成erp任务计划表，判断是否已经生成
            if(form.getWashStatus()==2) {
                List<ErpClientWashPlanDTO> erpClientWashPlanDTOList = erpClientWashPlanApi.findListByFmwcId(flowMonthWashControlDTO.getId());
                if (CollUtil.isEmpty(erpClientWashPlanDTOList)) {
                    String tableSuffix = flowMonthWashControlDTO.getYear() + "" + String.format("%02d", flowMonthWashControlDTO.getMonth());
                    List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList = new ArrayList<>();
                    List<CrmEnterpriseDTO> crmEnterpriseDTOList = pageCrmEnterprise(tableSuffix);
                    List<ErpClientDTO> erpClientDTOList = erpClientApi.getByCrmEnterpriseIdList(crmEnterpriseDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()));
                    for (ErpClientDTO erpClientDTO : erpClientDTOList) {
                        SaveOrUpdateErpClientWashPlanRequest erpClient = new SaveOrUpdateErpClientWashPlanRequest();
                        erpClient.setCrmEnterpriseId(erpClientDTO.getCrmEnterpriseId());
                        erpClient.setName(erpClientDTO.getClientName());
                        erpClient.setFmwcId(flowMonthWashControlDTO.getId());
                        erpClient.setStatus(1);
                        erpClient.setFlowMode(erpClientDTO.getFlowMode());
                        erpClient.setEid(erpClientDTO.getRkSuId());
                        erpClient.setOpUserId(userInfo.getCurrentUserId());
                        erpClientWashPlanList.add(erpClient);
                    }
                    erpClientWashPlanApi.generate(erpClientWashPlanList);
                }
            }else if(form.getWashStatus()==3){
                request.setWashTime(new Date());
            }
            request.setWashStatus(form.getWashStatus());
        }else if(form.getStage()==5){
            if (form.getGbLockStatus() == 2) {
                UpdateStageRequest updateStageRequest = new UpdateStageRequest();
                updateStageRequest.setId(form.getId());
                updateStageRequest.setOpUserId(userInfo.getCurrentUserId());
                sjmsFlowMonthWashControlApi.gbLockStatus(updateStageRequest);
            }else if(form.getGbLockStatus() == 3){
                request.setGbLockTime(new Date());
            }
            request.setGbLockStatus(form.getGbLockStatus());
        }else if(form.getStage()==6){
            if (form.getUnlockStatus() == 2) {
                UpdateStageRequest updateStageRequest = new UpdateStageRequest();
                updateStageRequest.setId(form.getId());
                updateStageRequest.setOpUserId(userInfo.getCurrentUserId());
                sjmsFlowMonthWashControlApi.unlockStatus(updateStageRequest);
            }else if(form.getUnlockStatus() == 3){
                request.setUnlockTime(new Date());
            }
            request.setUnlockStatus(form.getUnlockStatus());
        }else if(form.getStage()==7){
            if (form.getGbUnlockStatus() == 2) {
                UpdateStageRequest updateStageRequest = new UpdateStageRequest();
                updateStageRequest.setId(form.getId());
                updateStageRequest.setOpUserId(userInfo.getCurrentUserId());
                sjmsFlowMonthWashControlApi.gbUnlockStatus(updateStageRequest);
            }else if(form.getGbUnlockStatus() == 3){
                request.setGbUnlockTime(new Date());
            }
            request.setGbUnlockStatus(form.getGbUnlockStatus());
        }else if(form.getStage()==8){
            if(form.getTaskStatus()==2){
                request.setTaskTime(new Date());
            }
            request.setTaskStatus(form.getTaskStatus());
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(flowMonthWashControlApi.saveOrUpdate(request));
    }

    /**
     * 获取当前是否可以编辑非锁规则
     *
     * @return FlowMonthWashControlDTO对象 1、对象!=null可以编辑 2、对象==null不能编辑
     */
    @ApiOperation(value = "是否可以进行非锁客户分类规则、非锁销量分配规则编辑")
    @GetMapping("/getBasisStatusUpdate")
    public Result<Boolean> getBasisStatusUpdate() {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO != null) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    /**
     * 获取当前是否可以编辑非锁规则
     *
     * @return FlowMonthWashControlDTO对象 1、对象!=null可以编辑 2、对象==null不能编辑
     */
    @ApiOperation(value = "是否可以编辑非锁销量分配")
    @GetMapping("/getUnlockStatus")
    public Result<Boolean> getUnlockStatus() {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }


    public List<CrmEnterpriseDTO> pageCrmEnterprise(String tableSuffix) {
        //查询所有erp对接的企业信息
        List<CrmEnterpriseDTO> suIdList = new ArrayList<>();
        QueryCrmEnterpriseBackUpPageRequest request = new QueryCrmEnterpriseBackUpPageRequest();
        request.setSupplyChainRole(AgencySupplyChainRoleEnum.SUPPLIER.getCode());
        request.setSoMonth(tableSuffix);
        request.setBusinessCode(1);
        //需要循环调用
        Page<CrmEnterpriseDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseApi.pageListSuffixBackUpInfo(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (CrmEnterpriseDTO crmEnterpriseDTO : page.getRecords()) {
                suIdList.add(crmEnterpriseDTO);
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return suIdList;
    }
}

