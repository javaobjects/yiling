package com.yiling.admin.cms.evaluate.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.content.vo.*;
import com.yiling.admin.cms.evaluate.form.AddHealthEvaluateForm;
import com.yiling.admin.cms.evaluate.form.PublishHealthEvaluateForm;
import com.yiling.admin.cms.evaluate.form.QueryHealthEvaluatePageForm;
import com.yiling.admin.cms.evaluate.form.UpdateHealthEvaluateForm;
import com.yiling.admin.cms.evaluate.vo.HealthEvaluateVO;
import com.yiling.cms.evaluate.api.HealthEvaluateApi;
import com.yiling.cms.evaluate.api.HealthEvaluateQuestionApi;
import com.yiling.cms.evaluate.api.HealthEvaluateResultApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateUserDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateRequest;
import com.yiling.cms.evaluate.dto.request.PublishHealthEvaluateRequest;
import com.yiling.cms.evaluate.dto.request.QueryHealthEvaluatePageRequest;
import com.yiling.cms.evaluate.dto.request.UpdateHealthEvaluateRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.dept.api.HospitalDeptApi;
import com.yiling.ih.dept.dto.HospitalDeptListDTO;
import com.yiling.ih.dept.dto.request.QueryHospitalDeptListRequest;
import com.yiling.ih.disease.api.DiseaseApi;
import com.yiling.ih.disease.dto.DiseaseDTO;
import com.yiling.ih.disease.dto.request.QueryDiseaseListRequest;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 健康测评 前端控制器
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Api(tags = "健康测评")
@RestController
@RequestMapping("/cms/healthEvaluate")
public class HealthEvaluateController extends BaseController {

    @DubboReference
    private HealthEvaluateApi healthEvaluateApi;

    @DubboReference
    private HealthEvaluateQuestionApi healthEvaluateQuestionApi;

    @Autowired
    private FileService fileService;

    @DubboReference
    DoctorApi doctorApi;

    @DubboReference
    private DiseaseApi diseaseApi;

    @DubboReference
    private HospitalDeptApi hospitalDeptApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private HealthEvaluateResultApi healthEvaluateResultApi;

    @Log(title = "添加健康测评", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加健康测评")
    @PostMapping("addHealthEvaluate")
    public Result<Long> addHealthEvaluate(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddHealthEvaluateForm form) {

        if (CollUtil.isEmpty(form.getLineIdList())) {
            throw new BusinessException(ResultCode.FAILED, "请选择引用业务线");
        }

        AddHealthEvaluateRequest request = PojoUtils.map(form, AddHealthEvaluateRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(healthEvaluateApi.addHealthEvaluate(request));
    }

    @Log(title = "编辑健康测评", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑健康测评")
    @PostMapping("updateHealthEvaluate")
    public Result<Boolean> updateHealthEvaluate(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateHealthEvaluateForm form) {
        UpdateHealthEvaluateRequest request = PojoUtils.map(form, UpdateHealthEvaluateRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        healthEvaluateApi.updateHealthEvaluate(request);
        return Result.success(true);
    }

    @Log(title = "发布健康测评", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("发布健康测评")
    @PostMapping("publishHealthEvaluate")
    public Result<Boolean> publishHealthEvaluate(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid PublishHealthEvaluateForm form) {

        // 0、获取测评设置
        HealthEvaluateDTO healthEvaluateDTO = healthEvaluateApi.getHealthEvaluateById(form.getId());
        // 如果有结果设置 -> 判断是否设置测评结果
        if(healthEvaluateDTO.getResultFlag() == 1) {
            List<HealthEvaluateResultDTO> resultList = healthEvaluateResultApi.getResultListByEvaluateId(form.getId());
            if(CollUtil.isEmpty(resultList)) {
                return Result.failed("请完善量表结果后再发布");
            }
        }

        // 1、查询量表下的题目设置
        List<HealthEvaluateQuestionDTO> questionList = healthEvaluateQuestionApi.getHealthEvaluateQuestionByEvaluateId(form.getId());
        if (CollUtil.isEmpty(questionList)) {
            return Result.failed("请完善量表题目后再发布");
        }

        // 2、更新发布状态
        PublishHealthEvaluateRequest request = PojoUtils.map(form, PublishHealthEvaluateRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        healthEvaluateApi.publishHealthEvaluate(request);
        return Result.success(true);
    }

    @ApiOperation("健康测评详情")
    @GetMapping("getDetailById")
    public Result<HealthEvaluateVO> getDetailById(@RequestParam Long id) {
        HealthEvaluateDTO evaluateDTO = healthEvaluateApi.getHealthEvaluateById(id);
        if (Objects.isNull(evaluateDTO)) {
            return Result.failed("未获取到健康测评详情");
        }
        HealthEvaluateVO evaluateVO = PojoUtils.map(evaluateDTO, HealthEvaluateVO.class);

        List<Long> lineIdList = healthEvaluateApi.getLineIdList(id);

        evaluateVO.setLineIdList(lineIdList);

        if (StrUtil.isNotEmpty(evaluateVO.getBackImage())) {
            evaluateVO.setBackImageUrl(fileService.getUrl(evaluateVO.getBackImage(), FileTypeEnum.HEALTH_BACK_IMAGE));
        }
        if (StrUtil.isNotEmpty(evaluateVO.getCoverImage())) {
            evaluateVO.setCoverImageUrl(fileService.getUrl(evaluateVO.getCoverImage(), FileTypeEnum.HEALTH_COVER_IMAGE));
        }
        // 获取医生名称
        if (Objects.nonNull(evaluateDTO.getDocId()) && evaluateDTO.getDocId() > 0) {
            DoctorAppInfoDTO doctorInfo = doctorApi.getDoctorInfoByDoctorId(evaluateDTO.getDocId().intValue());
            evaluateVO.setDocName(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getDoctorName).orElse(null));
            evaluateVO.setHospitalName(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getHospitalName).orElse(null));
            evaluateVO.setHospitalDepartment(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getHospitalDepartment).orElse(null));
        }

        //关联疾病
        if (CollUtil.isNotEmpty(evaluateDTO.getDiseaseIdList())) {
            QueryDiseaseListRequest request = new QueryDiseaseListRequest();
            request.setIdList(evaluateDTO.getDiseaseIdList().stream().map(Long::intValue).collect(Collectors.toList()));
            request.setSize(100);
            Page<DiseaseDTO> diseaseDTOPage = diseaseApi.queryDisease(request);
            List<DiseaseVO> diseaseVOList = PojoUtils.map(diseaseDTOPage.getRecords(), DiseaseVO.class);
            evaluateVO.setDiseaseVOList(diseaseVOList);
        }

        //关联科室
        if (CollUtil.isNotEmpty(evaluateDTO.getDeptIdList())) {
            QueryHospitalDeptListRequest request = new QueryHospitalDeptListRequest();
            request.setIds(evaluateDTO.getDeptIdList().stream().map(Long::intValue).collect(Collectors.toList()));
            List<HospitalDeptListDTO> hospitalDeptListDTOS = hospitalDeptApi.listByIds(request);
            List<HospitalDeptVO> hospitalDeptVOS = PojoUtils.map(hospitalDeptListDTOS, HospitalDeptVO.class);
            evaluateVO.setHospitalDeptVOS(hospitalDeptVOS);
        }

        return Result.success(evaluateVO);
    }

    @ApiOperation("健康测评列表")
    @GetMapping("queryEvaluatePage")
    public Result<Page<HealthEvaluateVO>> queryEvaluatePage(@Valid QueryHealthEvaluatePageForm form) {
        form.build();
        QueryHealthEvaluatePageRequest request = PojoUtils.map(form, QueryHealthEvaluatePageRequest.class);
        Page<HealthEvaluateDTO> healthEvaluateDTOPage = healthEvaluateApi.listPage(request);
        if (healthEvaluateDTOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        List<Long> createUserIds = healthEvaluateDTOPage.getRecords().stream().map(HealthEvaluateDTO::getCreateUser).collect(Collectors.toList());
        List<Long> updateUserIds = healthEvaluateDTOPage.getRecords().stream().map(HealthEvaluateDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Long> healthEvaluateIdList = healthEvaluateDTOPage.getRecords().stream().map(HealthEvaluateDTO::getId).distinct().collect(Collectors.toList());
        List<Long> userIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Page<HealthEvaluateVO> healthEvaluateVOPage = PojoUtils.map(healthEvaluateDTOPage, HealthEvaluateVO.class);

        // 获取测评参与人数
        List<HealthEvaluateUserDTO> evaluateUserDTOList = healthEvaluateApi.getUserByEvaluateIdList(healthEvaluateIdList);
        Map<Long, List<HealthEvaluateUserDTO>> evaluateUserMap = evaluateUserDTOList.stream().collect(Collectors.groupingBy(HealthEvaluateUserDTO::getHealthEvaluateId));

        healthEvaluateVOPage.getRecords().forEach(evaluateVO -> {
            evaluateVO.setCreateUserName(userDTOMap.getOrDefault(evaluateVO.getCreateUser(), new UserDTO()).getName());
            evaluateVO.setUpdateUserName(userDTOMap.getOrDefault(evaluateVO.getUpdateUser(), new UserDTO()).getName());

            if(evaluateUserMap.containsKey(evaluateVO.getId())) {
                List<HealthEvaluateUserDTO> userList = evaluateUserMap.get(evaluateVO.getId());
                long userCount = userList.stream().map(HealthEvaluateUserDTO::getCreateUser).distinct().count();

                List<HealthEvaluateUserDTO> finishList = userList.stream().filter(user -> user.getFinishFlag() == 1).collect(Collectors.toList());
                long finishCount = finishList.stream().map(HealthEvaluateUserDTO::getCreateUser).distinct().count();

                evaluateVO.setUserCount(userCount);
                evaluateVO.setFinishCount(finishCount);
                evaluateVO.setFinishDistinctCount((long)finishList.size());
            }
        });



        return Result.success(healthEvaluateVOPage);
    }


}
