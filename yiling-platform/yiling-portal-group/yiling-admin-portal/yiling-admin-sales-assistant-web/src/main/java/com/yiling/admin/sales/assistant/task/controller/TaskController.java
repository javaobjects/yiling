package com.yiling.admin.sales.assistant.task.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.sales.assistant.task.form.AddCommissionRuleForm;
import com.yiling.admin.sales.assistant.task.form.AddTaskForm;
import com.yiling.admin.sales.assistant.task.form.DeleteDeptForm;
import com.yiling.admin.sales.assistant.task.form.DeleteGoodsForm;
import com.yiling.admin.sales.assistant.task.form.DeleteTaskDistributorForm;
import com.yiling.admin.sales.assistant.task.form.ImportTaskGoodsForm;
import com.yiling.admin.sales.assistant.task.form.QueryDistributorPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryGoodsPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryLockTerminalListForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskDistributorPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskMemberPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskRegisterTerminalForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskRegisterUserForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskTraceOrderForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskTraceTaskUserForm;
import com.yiling.admin.sales.assistant.task.form.StopTaskForm;
import com.yiling.admin.sales.assistant.task.form.UpdateTaskForm;
import com.yiling.admin.sales.assistant.task.handler.ImportTaskGoodsDataHandler;
import com.yiling.admin.sales.assistant.task.vo.CommissionRuleVO;
import com.yiling.admin.sales.assistant.task.vo.DeptTreeVO;
import com.yiling.admin.sales.assistant.task.vo.DistributorVO;
import com.yiling.admin.sales.assistant.task.vo.ImportGoodsResultVO;
import com.yiling.admin.sales.assistant.task.vo.LockTerminalListVO;
import com.yiling.admin.sales.assistant.task.vo.MemberStageVO;
import com.yiling.admin.sales.assistant.task.vo.MemberVO;
import com.yiling.admin.sales.assistant.task.vo.TaskAreaVO;
import com.yiling.admin.sales.assistant.task.vo.TaskCountVO;
import com.yiling.admin.sales.assistant.task.vo.TaskDeptVO;
import com.yiling.admin.sales.assistant.task.vo.TaskDetailVO;
import com.yiling.admin.sales.assistant.task.vo.TaskMemberRecordVO;
import com.yiling.admin.sales.assistant.task.vo.TaskSelectGoodsVO;
import com.yiling.admin.sales.assistant.task.vo.TaskTraceOrderVO;
import com.yiling.admin.sales.assistant.task.vo.TaskTraceRegisterUserVO;
import com.yiling.admin.sales.assistant.task.vo.TaskTraceTerminalVO;
import com.yiling.admin.sales.assistant.task.vo.TaskTraceUserVO;
import com.yiling.admin.sales.assistant.task.vo.TaskTraceVO;
import com.yiling.admin.sales.assistant.task.vo.TaskVO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dto.DistributorDTO;
import com.yiling.sales.assistant.task.dto.LockTerminalListDTO;
import com.yiling.sales.assistant.task.dto.TaskAreaDTO;
import com.yiling.sales.assistant.task.dto.TaskCountDTO;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.dto.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.TaskMemberRecordDTO;
import com.yiling.sales.assistant.task.dto.TaskRuleDTO;
import com.yiling.sales.assistant.task.dto.TaskSelectGoodsDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceOrderDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceRegisterUserDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceTerminalDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceUserDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskRuleRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteDeptRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryLockTerminalListRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskMemberPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterTerminalRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterUserRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceTaskUserRequest;
import com.yiling.sales.assistant.task.dto.request.StopTaskRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskRequest;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.framework.common.util.Constants.YILING_EID;

/**
 * @author: ray
 * @date: 2021/9/13
 */
@Api(tags = "任务")
@RestController
@Validated
@Slf4j
@RequestMapping("task")
public class TaskController extends BaseController {
    @DubboReference
    TaskApi taskApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    GoodsLimitPriceApi goodsLimitPriceApi;

    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private DepartmentApi departmentApi;

    @Autowired
    private ImportTaskGoodsDataHandler importTaskGoodsDataHandler;

    @ApiOperation(value = "创建任务")
    @PostMapping("add")
    @Log(title = "创建任务", businessType = BusinessTypeEnum.INSERT)
    public Result addTask(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody AddTaskForm addTaskForm) {
        // 佣金政策处理
        AddTaskRequest addTaskRequest = new AddTaskRequest();
        addTaskRequest.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(addTaskForm, addTaskRequest);
        AddTaskRuleRequest addFinishType = new AddTaskRuleRequest();
        addFinishType.setRuleKey(RuleKeyEnum.FINISH_TYPE.toString()).setRuleType(RuleTypeEnum.FINISH_TYPE.getCode()).setRuleValue(addTaskForm.getFinishType().toString());
        addTaskRequest.getAddTaskRuleList().add(addFinishType);
        if (CollUtil.isNotEmpty(addTaskForm.getAddCommissionRuleFormList())) {
            AddTaskRuleRequest addTaskRuleRequest = new AddTaskRuleRequest();
            List<String> list = addTaskForm.getAddCommissionRuleFormList().stream().map(AddCommissionRuleForm::getCommission).collect(Collectors.toList());
            List<String> collect = list.stream().distinct().collect(Collectors.toList());
            if(list.size()>collect.size()){
                throw new BusinessException(ResultCode.FAILED,"阶梯条件，佣金不能重复");
            }
            // List<String> conlist = addTaskForm.getAddCommissionRuleFormList().stream().map(AddCommissionRuleForm::getCommissionCondition).collect(Collectors.toList());
            String commission = String.join(",", list);
            //String condition = String.join(",",conlist);
            addTaskRuleRequest.setRuleKey(RuleKeyEnum.COMMISSION.toString()).setRuleType(RuleTypeEnum.COMMISSION.getCode()).setRuleValue(commission);
            //addTaskRuleRequest.setRuleType(RuleTypeEnum.FINISH_TYPE.getCode()).setRuleKey(RuleKeyEnum.SALE_CONDITION.toString()).setRuleValue(condition);
            addTaskRequest.getAddTaskRuleList().add(addTaskRuleRequest);
        }
        taskApi.addTask(addTaskRequest);
        return Result.success();
    }

    @ApiOperation(value = "创建任务-选择配送商")
    @GetMapping("queryDistributorPage")
    public Result<Page<DistributorVO>> queryDistributorPage(QueryDistributorPageForm queryDistributorPageForm) {
        QueryEnterprisePageListRequest request = PojoUtils.map(queryDistributorPageForm, QueryEnterprisePageListRequest.class);
        request.setMallFlag(1);
        request.setInTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode()));

        //获取客户是否管控
        QueryLimitFlagRequest limitFlagRequest = new QueryLimitFlagRequest();
        limitFlagRequest.setEid(YILING_EID);
        limitFlagRequest.setCustomerEids(Lists.newArrayList());
        if (queryDistributorPageForm.getType() > 0) {
            limitFlagRequest.setLimitFlag(1);
        }
        List<CustomerPriceLimitDTO> customerPriceLimitDTOList = goodsLimitPriceApi.getCustomerLimitFlagByEidAndCustomerEid(limitFlagRequest);
        Page<DistributorVO> newPage = new Page<>();
        if (CollUtil.isEmpty(customerPriceLimitDTOList)) {
            return Result.success(newPage);
        }
        Map<Long, CustomerPriceLimitDTO> flagMap = customerPriceLimitDTOList.stream().collect(Collectors.toMap(CustomerPriceLimitDTO::getCustomerEid, Function.identity()));
        if(queryDistributorPageForm.getType() == 1){
            request.setIds(new ArrayList<>(flagMap.keySet()));
        }else if(queryDistributorPageForm.getType() == 2){
            request.setNotInIds(new ArrayList<>(flagMap.keySet()));
        }

        request.setType(0);
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);

        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<DistributorVO> list = new ArrayList<>();

            page.getRecords().forEach(e -> {
                DistributorVO customerLimitVO = new DistributorVO();
                customerLimitVO.setDistributorEid(e.getId());
                customerLimitVO.setName(e.getName());
                if (flagMap.containsKey(e.getId())) {
                    if (flagMap.get(customerLimitVO.getDistributorEid()).getLimitFlag() == 1) {
                        customerLimitVO.setType(1);
                    } else {
                        customerLimitVO.setType(2);
                    }
                } else {
                    customerLimitVO.setType(2);
                }
                list.add(customerLimitVO);
            });
            newPage.setRecords(list);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "任务详情-配送商反显")
    @GetMapping("queryTaskDistributorPage")
    public Result<Page<DistributorVO>> queryTaskDistributorPage(QueryTaskDistributorPageForm queryDistributorPageForm) {
        QueryTaskDistributorPageRequest queryDistributorPageRequest = new QueryTaskDistributorPageRequest();
        PojoUtils.map(queryDistributorPageForm, queryDistributorPageRequest);
        Page<DistributorDTO> distributorDTOPage = taskApi.listTaskDistributorPage(queryDistributorPageRequest);
        Page<DistributorVO> distributorVOPage = PojoUtils.map(distributorDTOPage, DistributorVO.class);
        return Result.success(distributorVOPage);
    }


    @ApiOperation(value = "创建任务-选择商品")
    @GetMapping("queryGoodsForAdd")
    public Result<Page<TaskSelectGoodsVO>> queryGoodsForAdd(QueryGoodsPageForm queryGoodsPageForm) {
        QueryGoodsPageRequest queryGoodsPageRequest = new QueryGoodsPageRequest();
        PojoUtils.map(queryGoodsPageForm, queryGoodsPageRequest);
        Page<TaskSelectGoodsDTO> taskSelectGoodsDTO = taskApi.queryGoodsForAdd(queryGoodsPageRequest);
        Page<TaskSelectGoodsVO> taskSelectGoodsVOPage = PojoUtils.map(taskSelectGoodsDTO, TaskSelectGoodsVO.class);
        return Result.success(taskSelectGoodsVOPage);
    }
    @ApiOperation(value = "导入商品", httpMethod = "POST")
    @PostMapping(value = "importTaskGoods", headers = "content-type=multipart/form-data")
    @Log(title = "导入任务商品", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportGoodsResultVO>  importTaskGoods(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestParam(value = "file") MultipartFile file){
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setSaveUrl(ExeclImportUtils.EXECL_PATH);
        params.setNeedVerify(false);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }
        ImportResultModel<ImportTaskGoodsForm> importResultModel;
        try {
            //包含了插入数据库失败的信息
            Long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, currentAdminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportTaskGoodsForm.class, params, importTaskGoodsDataHandler, paramMap);
            log.info("任务商品导入耗时：{}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        List<ImportTaskGoodsForm> list = importResultModel.getList();
        List<Long> goodsIds = list.stream().map(ImportTaskGoodsForm::getGid).collect(Collectors.toList());
        List<GoodsDTO> goodsInfoDTOS = goodsApi.batchQueryInfo(goodsIds);

        List<GoodsYilingPriceDTO> priceParamNameList = goodsYilingPriceApi.getPriceParamNameList(goodsIds, new Date());
        List<TaskSelectGoodsVO> taskSelectGoodsVOS = Lists.newArrayList();
        list.forEach(importTaskGoodsForm -> {
            TaskSelectGoodsVO taskSelectGoodsVO = new TaskSelectGoodsVO();
            if(StrUtil.isNotEmpty(importTaskGoodsForm.getErrorMsg())){
                return;
            }
            taskSelectGoodsVOS.add(taskSelectGoodsVO);
            GoodsDTO goodsInfoDTO = goodsInfoDTOS.stream().filter(g -> g.getId().equals(importTaskGoodsForm.getGid()))
                    .findAny().orElse(null);
            if (!Objects.isNull(goodsInfoDTO)) {
                taskSelectGoodsVO.setPrice(goodsInfoDTO.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()).setGoodsId(importTaskGoodsForm.getGid()).setSellSpecifications(goodsInfoDTO.getSellSpecifications());
            }
            taskSelectGoodsVO.setGoodsName(goodsInfoDTO.getName());
            GoodsYilingPriceDTO goodsYilingPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(taskSelectGoodsVO.getGoodsId()) && p.getParamId().equals(1L)).findAny().orElse(null);
            if(Objects.nonNull(goodsYilingPriceDTO)){
                taskSelectGoodsVO.setOutPrice(NumberUtil.round(goodsYilingPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
            }else{
                taskSelectGoodsVO.setOutPrice(BigDecimal.ZERO);
            }
            GoodsYilingPriceDTO goodsSellPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(taskSelectGoodsVO.getGoodsId()) && p.getParamId().equals(3L)).findAny().orElse(null);
            //商销售价格
            if(Objects.nonNull(goodsSellPriceDTO)){
                taskSelectGoodsVO.setSellPrice(NumberUtil.round(goodsSellPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
            }else{
                taskSelectGoodsVO.setSellPrice(BigDecimal.ZERO);
            }

        });
        ImportGoodsResultVO importGoodsResultVO = PojoUtils.map(importResultModel, ImportGoodsResultVO.class);
        importGoodsResultVO.setSuccessList(taskSelectGoodsVOS);
        return Result.success(importGoodsResultVO);
    }

    @ApiOperation(value = "编辑任务")
    @PostMapping("update")
    @Log(title = "编辑任务", businessType = BusinessTypeEnum.UPDATE)
    public Result updateTask(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody UpdateTaskForm updateTaskForm) {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(updateTaskForm, updateTaskRequest);
        if (CollUtil.isNotEmpty(updateTaskForm.getAddCommissionRuleFormList())) {
            AddTaskRuleRequest addTaskRuleRequest = new AddTaskRuleRequest();
            List<String> list = updateTaskForm.getAddCommissionRuleFormList().stream().map(AddCommissionRuleForm::getCommission).collect(Collectors.toList());
            List<String> collect = list.stream().distinct().collect(Collectors.toList());
            if(list.size()>collect.size()){
                throw new BusinessException(ResultCode.FAILED,"阶梯条件，佣金不能重复");
            }
            String commission = String.join(",", list);
            addTaskRuleRequest.setRuleKey(RuleKeyEnum.COMMISSION.toString()).setRuleType(RuleTypeEnum.COMMISSION.getCode()).setRuleValue(commission);
            if (CollUtil.isEmpty(updateTaskRequest.getAddTaskRuleList())) {
                updateTaskRequest.setAddTaskRuleList(Lists.newArrayList());
            }
            updateTaskRequest.getAddTaskRuleList().add(addTaskRuleRequest);
        }
        taskApi.updateTask(updateTaskRequest);
        return Result.success();
    }

    @ApiOperation(value = "获取任务数量")
    @PostMapping("getTaskCount")
    public Result<TaskCountVO> getTaskCount() {
        TaskCountDTO taskCountDTO = taskApi.getTaskCount();
        return Result.success(PojoUtils.map(taskCountDTO, TaskCountVO.class));
    }

    @ApiOperation(value = "移除部门")
    @PostMapping("deleteDept")
    public Result<BoolObject> deleteDept(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody DeleteDeptForm deleteDeptForm) {
        Integer taskStstus = taskApi.getTaskStatusByDeptOrGoods(deleteDeptForm.getTaskDeptUserId(), 1);
        if (!taskStstus.equals(TaskStatusEnum.UNSTART.getStatus())) {
            return Result.failed("只有未开始任务可以修改");
        }
        DeleteDeptRequest request = PojoUtils.map(deleteDeptForm, DeleteDeptRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean b = taskApi.deleteDept(request);
        return Result.success(new BoolObject(b));
    }

    @ApiOperation(value = "移除任务商品")
    @PostMapping("deleteGoods")
    public Result<BoolObject> deleteGoods(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody DeleteGoodsForm deleteGoodsForm) {
        Integer taskStstus = taskApi.getTaskStatusByDeptOrGoods(deleteGoodsForm.getTaskGoodsId(), 2);
        if (!taskStstus.equals(TaskStatusEnum.UNSTART.getStatus())) {
            return Result.failed("只有未开始任务可以修改");
        }
        DeleteGoodsRequest request = PojoUtils.map(deleteGoodsForm, DeleteGoodsRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean b = taskApi.deleteGoods(request);
        return Result.success(new BoolObject(b));
    }


    @ApiOperation("查询任务详情")
    @GetMapping("getDetailById")
    public Result<TaskDetailVO> getDetailById(@RequestParam Long id) {
        TaskDetailDTO taskDetailDTO = taskApi.getDetailById(id);
        TaskRuleDTO commissionRule = taskDetailDTO.getCommissionRuleVOList().stream().filter(t -> t.getRuleKey().equals(RuleKeyEnum.COMMISSION.toString())).findFirst().orElse(null);
        TaskDetailVO taskDetailVO = new TaskDetailVO();
        TaskRuleDTO commissionConditionRule = taskDetailDTO.getFinishRuleVOList().stream().filter(t -> t.getRuleKey().equals(RuleKeyEnum.SALE_CONDITION.toString())).findFirst().orElse(null);
        TaskRuleDTO step = taskDetailDTO.getFinishRuleVOList().stream().filter(t -> t.getRuleKey().equals(RuleKeyEnum.STEP_CONDITION.toString())).findAny().orElse(null);
        //阶梯佣金政策处理
        if (!Objects.isNull(commissionRule) && !Objects.isNull(commissionConditionRule) && Objects.nonNull(step) && step.getRuleValue().equals(TaskConstant.ONE)) {
            List<CommissionRuleVO> commRuleVOList = Lists.newArrayListWithExpectedSize(2);
            List<String> comm = Arrays.asList(commissionRule.getRuleValue().split(","));
            List<String> commCondition = Arrays.asList(commissionConditionRule.getRuleValue().split(","));
            for (int i = 0; i < comm.size(); i++) {
                CommissionRuleVO commissionRuleVO = new CommissionRuleVO();
                commissionRuleVO.setCommission(comm.get(i));
                commissionRuleVO.setCommissionCondition(commCondition.get(i));
                commRuleVOList.add(commissionRuleVO);
            }
            taskDetailVO.setCommRuleVOList(commRuleVOList);
        }
        UserDTO userDTO = userApi.getById(taskDetailDTO.getCreateUser());
        if (Objects.nonNull(userDTO)) {
            taskDetailVO.setCreatedBy(userDTO.getName());
        }
        PojoUtils.map(taskDetailDTO, taskDetailVO);
        if(CollUtil.isNotEmpty(taskDetailVO.getDeptIdList())){
            List<TaskDeptVO> taskDeptVOList = Lists.newArrayList();
            taskDetailVO.getDeptIdList().forEach(deptId->{
                TaskDeptVO taskDeptVO = new TaskDeptVO();
                taskDeptVO.setDeptId(deptId);
                EnterpriseDepartmentDTO departmentDTO = departmentApi.getById(deptId);
                taskDeptVO.setName(departmentDTO.getName());
                taskDeptVOList.add(taskDeptVO);
            });
            taskDetailVO.setTaskDeptVOS(taskDeptVOList);
        }
        return Result.success(taskDetailVO);
    }


    @ApiOperation(value = "任务区域反显", httpMethod = "GET")
    @GetMapping("queryTaskArea")
    public Result<CollectionObject<List<TaskAreaVO>>> queryTaskArea(@NotNull Long taskId) {
        List<TaskAreaDTO> taskAreaDTOS = taskApi.queryTaskArea(taskId);
        List<TaskAreaVO> taskAreaVOS = PojoUtils.map(taskAreaDTOS, TaskAreaVO.class);
        CollectionObject<List<TaskAreaVO>> result = new CollectionObject(taskAreaVOS);

        return Result.success(result);
    }

    @ApiOperation("任务追踪- 获取锁定的终端列表")
    @PostMapping("getLockTerminalList")
    public Result<Page<LockTerminalListVO>> getLockTerminalList(@Valid @RequestBody QueryLockTerminalListForm queryLockTerminalListForm) {
        QueryLockTerminalListRequest request = PojoUtils.map(queryLockTerminalListForm, QueryLockTerminalListRequest.class);
        Page<LockTerminalListDTO> pageList = taskApi.getLockTerminalList(request);
        List<LockTerminalListDTO> records = pageList.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }
        //终端主键（enterprise_id ）
        List<Long> terminalIdList = records.stream().map(LockTerminalListDTO::getTerminalId).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(terminalIdList);
        Map<Long, EnterpriseDTO> dtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        List<LockTerminalListVO> list = Lists.newArrayList();
        records.forEach(e -> {
            LockTerminalListVO listVO = PojoUtils.map(e, LockTerminalListVO.class);
            EnterpriseDTO enterpriseDTO = dtoMap.get(e.getTerminalId());
            listVO.setContactor(enterpriseDTO.getContactor());
            listVO.setContactorPhone(enterpriseDTO.getContactorPhone());
            list.add(listVO);
        });
        Page<LockTerminalListVO> page = PojoUtils.map(pageList, LockTerminalListVO.class);
        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation("任务列表")
    @GetMapping("queryTaskListPage")
    public Result<Page<TaskVO>> queryTaskListPage(QueryTaskPageForm queryTaskPageForm) {
        QueryTaskPageRequest queryTaskPageRequest = new QueryTaskPageRequest();
        PojoUtils.map(queryTaskPageForm, queryTaskPageRequest);
        if (queryTaskPageForm.getTaskStatus().equals(-1)) {
            queryTaskPageRequest.setTaskStatus(null);
        }
        if (queryTaskPageForm.getFinishType().equals(-1)) {
            queryTaskPageRequest.setFinishType(null);
        }
        Page<TaskDTO> taskDTOPage = taskApi.queryTaskListPage(queryTaskPageRequest);
        List<TaskDTO> records = taskDTOPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(queryTaskPageRequest.getPage());
        }
        List<Long> createUserIds = records.stream().map(TaskDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIds = records.stream().map(TaskDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Long> opUserIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(opUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        records.forEach(taskDTO -> {
            taskDTO.setCreatedBy(userDTOMap.getOrDefault(taskDTO.getCreateUser(), new UserDTO()).getName());
            taskDTO.setUpdatedBy(userDTOMap.getOrDefault(taskDTO.getUpdateUser(), new UserDTO()).getName());
        });
        Page<TaskVO> taskVOPage = PojoUtils.map(taskDTOPage, TaskVO.class);
        return Result.success(taskVOPage);
    }
/*    @ApiOperation("任务置顶")
    @PostMapping("setTop")
    public Result  setTop(@RequestBody  SetTopTaskForm setTopTaskForm){
        taskApi.setTop(setTopTaskForm.getId());
        return Result.success();
    }*/

    @ApiOperation("任务追踪-头部-右边数据统计")
    @GetMapping("getTaskTrace")
    public Result<TaskTraceVO> getTaskTrace(Long taskId) {
        TaskTraceDTO taskTraceDTO = taskApi.queryTaskTrace(taskId);
        TaskTraceVO taskTraceVO = new TaskTraceVO();
        PojoUtils.map(taskTraceDTO, taskTraceVO);
        return Result.success(taskTraceVO);
    }

    @ApiOperation("任务追踪-承接人员明细")
    @GetMapping("queryTaskUserPage")
    public Result<Page<TaskTraceUserVO>> queryTaskUserPage(QueryTaskTraceTaskUserForm queryTaskTraceTaskUserForm) {
        QueryTaskTraceTaskUserRequest queryTaskTraceTaskUserRequest = new QueryTaskTraceTaskUserRequest();
        PojoUtils.map(queryTaskTraceTaskUserForm, queryTaskTraceTaskUserRequest);
        Page<TaskTraceUserDTO> taskTraceUserDTOPage = taskApi.queryTaskUser(queryTaskTraceTaskUserRequest);
        Page<TaskTraceUserVO> taskTraceUserVOPage = PojoUtils.map(taskTraceUserDTOPage, TaskTraceUserVO.class);
        return Result.success(taskTraceUserVOPage);
    }

    /*    @ApiOperation("任务追踪-任务品完成进度查看")
        @GetMapping("listTaskTraceGoods")
        public Result<CollectionObject<List<TaskTraceGoodsVO>>> listTaskTraceGoods(QueryTaskTraceGoodsForm queryTaskTraceGoodsForm){
            QueryTaskTraceGoodsRequest queryTaskTraceGoodsRequest = new QueryTaskTraceGoodsRequest();
            PojoUtils.map(queryTaskTraceGoodsForm,queryTaskTraceGoodsRequest);
            List<TaskTraceGoodsDTO> taskTraceGoodsDTOList = taskApi.listTaskTraceGoods(queryTaskTraceGoodsRequest);
            List<TaskTraceGoodsVO> taskTraceGoodsVOList = Lists.newArrayList();
            taskTraceGoodsVOList = PojoUtils.map(taskTraceGoodsDTOList,TaskTraceGoodsVO.class);
            return Result.success(new CollectionObject(taskTraceGoodsVOList));
        }*/
    @ApiOperation("任务追踪-详情页-交易额交易量销售记录")
    @GetMapping("listTaskTraceOrderPage")
    public Result<Page<TaskTraceOrderVO>> listTaskTraceOrderPage(QueryTaskTraceOrderForm queryTaskTraceOrderForm) {
        QueryTaskTraceOrderRequest queryTaskTraceOrderRequest = new QueryTaskTraceOrderRequest();
        PojoUtils.map(queryTaskTraceOrderForm, queryTaskTraceOrderRequest);
        Page<TaskTraceOrderDTO> taskTraceOrderDTOPage = taskApi.listTaskTraceOrderPage(queryTaskTraceOrderRequest);
        Page<TaskTraceOrderVO> taskTraceOrderVOPage = PojoUtils.map(taskTraceOrderDTOPage, TaskTraceOrderVO.class);
        return Result.success(taskTraceOrderVOPage);
    }

    @ApiOperation("任务追踪-详情页-拉人类")
    @GetMapping("listTaskRegisterUserPage")
    public Result<Page<TaskTraceRegisterUserVO>> listTaskRegisterUserPage(QueryTaskRegisterUserForm queryTaskRegisterUserForm) {
        QueryTaskRegisterUserRequest queryTaskRegisterUserRequest = new QueryTaskRegisterUserRequest();
        PojoUtils.map(queryTaskRegisterUserForm, queryTaskRegisterUserRequest);
        Page<TaskTraceRegisterUserDTO> taskTraceRegisterUserDTOPage = taskApi.listTaskRegisterUserPage(queryTaskRegisterUserRequest);
        Page<TaskTraceRegisterUserVO> taskTraceRegisterUserVOPage = PojoUtils.map(taskTraceRegisterUserDTOPage, TaskTraceRegisterUserVO.class);
        return Result.success(taskTraceRegisterUserVOPage);
    }

    @ApiOperation("任务追踪-详情页-拉户类")
    @GetMapping("listTaskTraceTerminalPage")
    public Result<Page<TaskTraceTerminalVO>> listTaskTraceTerminalPage(QueryTaskRegisterTerminalForm queryTaskRegisterTerminalForm) {
        QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest = new QueryTaskRegisterTerminalRequest();
        PojoUtils.map(queryTaskRegisterTerminalForm, queryTaskRegisterTerminalRequest);
        Page<TaskTraceTerminalDTO> taskTraceTerminalDTOPage = taskApi.listTaskTraceTerminalPage(queryTaskRegisterTerminalRequest);
        Page<TaskTraceTerminalVO> taskTraceTerminalVOPage = PojoUtils.map(taskTraceTerminalDTOPage, TaskTraceTerminalVO.class);
        return Result.success(taskTraceTerminalVOPage);
    }

    @ApiOperation("创建任务-选择会员")
    @GetMapping("queryMember")
    public Result<CollectionObject<List<MemberVO>>> queryMember() {
        // 选择会员
        List<MemberSimpleDTO> memberSimpleDTOS = memberApi.queryAllList();
        List<MemberVO> voList = new ArrayList<>();
        memberSimpleDTOS.forEach(memberSimpleDTO -> {
            MemberVO memberVO = new MemberVO();
            memberVO.setMemberId(memberSimpleDTO.getId());
            memberVO.setName(memberSimpleDTO.getName());
            voList.add(memberVO);
        });
        return Result.success(new CollectionObject(voList));
    }

    @ApiOperation("创建任务-根据所选会员-选择会员条件")
    @GetMapping("queryMemberStage")
    public Result<CollectionObject<List<MemberStageVO>>> queryMemberStage(Long memberId) {
        // 选择会员购买条件
        List<MemberStageVO> voList = new ArrayList<>();
        MemberDetailDTO member = memberApi.getMember(memberId);
        member.getMemberBuyStageList().forEach(memberBuyStageDTO -> {
            MemberStageVO memberStageVO = new MemberStageVO();
            memberStageVO.setMemberStageId(memberBuyStageDTO.getId());
            memberStageVO.setPrice(memberBuyStageDTO.getPrice());
            memberStageVO.setValidTime(memberBuyStageDTO.getValidTime());
            voList.add(memberStageVO);
        });
        return Result.success(new CollectionObject(voList));
    }

    /*    @ApiOperation("创建任务-选择活动")
        @GetMapping("queryMemberPromotion")
        public Result<CollectionObject<List<MemberPromotionVO>>> queryMemberPromotion(){
            // 选择活动
            List<MemberPromotionVO> voList = new ArrayList<>();
            return Result.success(new CollectionObject(voList));
        }*/
    @ApiOperation("任务追踪-推广类")
    @GetMapping("listTaskMemberPage")
    public Result<Page<TaskMemberRecordVO>> listTaskMemberPage(QueryTaskMemberPageForm queryTaskMemberPageForm) {
        QueryTaskMemberPageRequest queryTaskMemberPageRequest = new QueryTaskMemberPageRequest();
        PojoUtils.map(queryTaskMemberPageForm, queryTaskMemberPageRequest);
        Page<TaskMemberRecordDTO> recordDTOPage = taskApi.listTaskMemberPage(queryTaskMemberPageRequest);
        if (recordDTOPage.getTotal()==0) {
            return Result.success(queryTaskMemberPageRequest.getPage());
        }
        Page<TaskMemberRecordVO> taskTraceTerminalVOPage = PojoUtils.map(recordDTOPage, TaskMemberRecordVO.class);
        List<TaskMemberRecordVO> taskMemberRecordVOS = taskTraceTerminalVOPage.getRecords();
        List<Long> updateUserIds = taskMemberRecordVOS.stream().map(TaskMemberRecordVO::getContactorUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(updateUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        taskMemberRecordVOS.forEach(taskMemberRecordVO -> {
            taskMemberRecordVO.setContactor(userDTOMap.getOrDefault(taskMemberRecordVO.getContactorUserId(), new UserDTO()).getName());
        });
        return Result.success(taskTraceTerminalVOPage);
    }

    @ApiOperation("移除任务所选配送商")
    @PostMapping("deleteTaskDistributor")
    @Log(title = "移除任务所选配送商", businessType = BusinessTypeEnum.UPDATE)
    public Result deleteTaskDistributor(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody DeleteTaskDistributorForm deleteTaskDistributorForm) {
        DeleteTaskDistributorRequest deleteTaskDistributorRequest = new DeleteTaskDistributorRequest();
        PojoUtils.map(deleteTaskDistributorForm, deleteTaskDistributorRequest);
        deleteTaskDistributorRequest.setOpUserId(currentAdminInfo.getCurrentUserId());
        taskApi.deleteTaskDistributorById(deleteTaskDistributorRequest);
        return Result.success();
    }

    @ApiOperation("任务停用")
    @PostMapping("stopTask")
    @Log(title = "任务停用", businessType = BusinessTypeEnum.UPDATE)
    public Result stopTask(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody StopTaskForm stopTaskForm) {
        StopTaskRequest stopTaskRequest = new StopTaskRequest();
        stopTaskRequest.setOpUserId(currentAdminInfo.getCurrentUserId());
        stopTaskRequest.setId(stopTaskForm.getId());
        taskApi.stopTask(stopTaskRequest);
        return Result.success();
    }

    @ApiOperation("删除任务")
    @PostMapping("deleteTask")
    @Log(title = "删除任务", businessType = BusinessTypeEnum.DELETE)
    public Result deleteTask(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody StopTaskForm stopTaskForm) {
        StopTaskRequest stopTaskRequest = new StopTaskRequest();
        stopTaskRequest.setOpUserId(currentAdminInfo.getCurrentUserId());
        stopTaskRequest.setId(stopTaskForm.getId());
        taskApi.deleteTask(stopTaskRequest);
        return Result.success();
    }
    @ApiOperation("选择部门")
    @GetMapping("queryDeptTree")
    public Result<List<DeptTreeVO>> queryDeptTree(){
        List<Tree<Long>> treeList = departmentApi.listTreeByEid(Constants.YILING_EID, EnableStatusEnum.ENABLED);
        List<DeptTreeVO> treeVOS = PojoUtils.map(treeList,DeptTreeVO.class);
        return Result.success(treeVOS);
    }
}