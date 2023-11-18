package com.yiling.admin.sales.assistant.task.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.task.form.AuditAccompanyingBillForm;
import com.yiling.admin.sales.assistant.task.form.QueryAccompanyingBillForm;
import com.yiling.admin.sales.assistant.task.form.QueryMatchBillPageForm;
import com.yiling.admin.sales.assistant.task.form.QueryTaskAccompanyBillPageForm;
import com.yiling.admin.sales.assistant.task.vo.AccompanyingBillMatchVO;
import com.yiling.admin.sales.assistant.task.vo.AccompanyingBillVO;
import com.yiling.admin.sales.assistant.task.vo.TaskAccompanyingBillVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.AccompanyingBillMatchDTO;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.QueryAccompanyingBillPage;
import com.yiling.sales.assistant.task.dto.request.QueryMatchBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资料上报
 * @author: gxl
 * @date: 2023/1/10
 */
@Api(tags = "资料管理")
@RestController
@RequestMapping("accompanyingBill")
public class AccompanyingBillController extends BaseController {
    @DubboReference
    TaskApi taskApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @Autowired
    private FileService fileService;

    @ApiOperation("审核")
    @PostMapping("audit")
    public Result<Boolean> audit(@CurrentUser  CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AuditAccompanyingBillForm form){
        SaveAccompanyingBillRequest request = new SaveAccompanyingBillRequest();
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(form,request);
        request.setAuditUserId(currentAdminInfo.getCurrentUserId());
        taskApi.auditAccompanyingBill(request);
        return Result.success(true);
    }

    @ApiOperation("查看详情")
    @GetMapping("getById")
    public Result<AccompanyingBillVO> getById(@CurrentUser CurrentAdminInfo currentAdminInfo, Long id){
        AccompanyingBillDTO accompanyingBillDTO = taskApi.getAccompanyingBillDetailById(id);
        AccompanyingBillVO accompanyingBillVO = new AccompanyingBillVO();
        PojoUtils.map(accompanyingBillDTO,accompanyingBillVO);
        UserDTO userDTO = userApi.getById(accompanyingBillVO.getCreateUser());
        accompanyingBillVO.setCreateUserName(userDTO.getName());
        return Result.success(accompanyingBillVO);
    }

    @ApiOperation("资料审核列表")
    @GetMapping("queryPage")
    public Result<Page<AccompanyingBillVO>> queryAccompanyingBillPage(@CurrentUser CurrentAdminInfo currentAdminInfo,@Valid QueryAccompanyingBillForm form){
        QueryAccompanyingBillPage request = new QueryAccompanyingBillPage();
        PojoUtils.map(form,request);
        request.setUserId(currentAdminInfo.getCurrentUserId());
        Page<AccompanyingBillDTO> accompanyingBillDTOPage = taskApi.queryAccompanyingBillPage(request);
        List<AccompanyingBillDTO> records = accompanyingBillDTOPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }
        List<Long> recvEidList = records.stream().map(AccompanyingBillDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(recvEidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, v -> v, (v1, v2) -> v1));
        Page<AccompanyingBillVO> accompanyingBillVOPage = PojoUtils.map(accompanyingBillDTOPage,AccompanyingBillVO.class);
        List<Long> auditUserIds = records.stream().map(AccompanyingBillDTO::getAuditUserId).distinct().collect(Collectors.toList());
        List<Long> createUserIds = records.stream().map(AccompanyingBillDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> userIds = CollUtil.union(createUserIds, auditUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        accompanyingBillVOPage.getRecords().forEach(accompanyingBillVO -> {
            accompanyingBillVO.setAuditUserName(userDTOMap.getOrDefault(accompanyingBillVO.getAuditUserId(), new UserDTO()).getName());
            accompanyingBillVO.setCreateUserName(userDTOMap.getOrDefault(accompanyingBillVO.getCreateUser(), new UserDTO()).getName());
            if (accompanyingBillVO.getDistributorEid()>0) {
                EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(accompanyingBillVO.getDistributorEid());
                accompanyingBillVO.setDistributorEname(enterpriseDTO.getName());
            }
            accompanyingBillVO.setAccompanyingBillPic(fileService.getUrl(accompanyingBillVO.getAccompanyingBillPic(), FileTypeEnum.ACCOMPANYING_BILL_PIC));

        });
        return Result.success(accompanyingBillVOPage);
    }

    @ApiOperation("任务追踪-随货同行单-销售记录")
    @GetMapping("queryTaskAccompanyBillPage")
    public Result<Page<TaskAccompanyingBillVO>> queryTaskAccompanyBillPage(@CurrentUser CurrentAdminInfo currentAdminInfo,@Valid QueryTaskAccompanyBillPageForm form){
        QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest = new QueryTaskAccompanyBillPageRequest();
        queryTaskAccompanyBillPageRequest.setUserTaskId(form.getUserTaskId());
        Page<TaskAccompanyingBillDTO> taskAccompanyingBillDTOPage = taskApi.queryTaskAccompanyBillPage(queryTaskAccompanyBillPageRequest);
        Page<TaskAccompanyingBillVO> taskAccompanyingBillVOPage = PojoUtils.map(taskAccompanyingBillDTOPage,TaskAccompanyingBillVO.class);
        return Result.success(taskAccompanyingBillVOPage);
    }

    @ApiOperation("流向列表")
    @GetMapping("queryMatchBillPage")
    public Result<Page<AccompanyingBillMatchVO>> queryMatchBillPage(@CurrentUser CurrentAdminInfo currentAdminInfo,@Valid  QueryMatchBillPageForm queryMatchBillPageForm){
        QueryMatchBillPageRequest request = new QueryMatchBillPageRequest();
        PojoUtils.map(queryMatchBillPageForm,request);
        Page<AccompanyingBillMatchDTO> accompanyingBillMatchDTOPage = taskApi.queryMatchBillPage(request);
        Page<AccompanyingBillMatchVO> accompanyingBillMatchVOPage = PojoUtils.map(accompanyingBillMatchDTOPage,AccompanyingBillMatchVO.class);
        return Result.success(accompanyingBillMatchVOPage);
    }
    @ApiOperation("流向列表-查询详情")
    @GetMapping("getMatchBillDetail")
    public Result<AccompanyingBillMatchVO> getMatchBillDetail(@CurrentUser CurrentAdminInfo currentAdminInfo,Long id){
        AccompanyingBillMatchDTO matchBillDetail = taskApi.getMatchBillDetail(id);
        AccompanyingBillMatchVO accompanyingBillMatchVO = new AccompanyingBillMatchVO();
        PojoUtils.map(matchBillDetail,accompanyingBillMatchVO);
        return Result.success(accompanyingBillMatchVO);
    }
}