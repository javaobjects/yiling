package com.yiling.admin.cms.content.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.cms.content.form.*;
import com.yiling.admin.cms.content.vo.*;
import com.yiling.admin.cms.goods.vo.GoodsInfoVO;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.api.HMCContentApi;
import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.meeting.api.MeetingApi;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.enums.MeetingShowStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.ih.dept.api.HospitalDeptApi;
import com.yiling.ih.dept.dto.HospitalDeptDTO;
import com.yiling.ih.dept.dto.HospitalDeptListDTO;
import com.yiling.ih.dept.dto.request.QueryHospitalDeptListRequest;
import com.yiling.ih.disease.api.DiseaseApi;
import com.yiling.ih.disease.dto.DiseaseDTO;
import com.yiling.ih.disease.dto.request.QueryDiseaseListRequest;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.ih.user.dto.request.QueryDoctorPageRequest;
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
 * <p>
 * HMC内容 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Api(tags = "HMC内容管理")
@RestController
@RequestMapping("/hmc/content")
public class HMCContentController extends BaseController {

    @DubboReference
    private HMCContentApi hmcContentApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    DoctorApi doctorApi;

    @Log(title = "添加内容", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加")
    @PostMapping("addContent")
    public Result<Boolean> addContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddHmcContentForm form) {
        AddHmcContentRequest request = PojoUtils.map(form, AddHmcContentRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        hmcContentApi.addContent(request);
        return Result.success(true);
    }

    @ApiOperation("内容排序")
    @PostMapping("contentRank")
    public Result<Boolean> contentRank(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentRankTopForm form) {
        ContentRankRequest request = new ContentRankRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = hmcContentApi.contentRank(request);
        return Result.success(result);
    }

    @ApiOperation("修改引用")
    @PostMapping("updateReferStatus")
    public Result<Boolean> updateReferStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentReferStatusForm form) {
        ContentReferStatusRequest request = new ContentReferStatusRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = hmcContentApi.updateReferStatus(request);
        return Result.success(result);
    }

    @ApiOperation("列表")
    @PostMapping("queryContentPage")
    public Result<Page<HMCContentVO>> queryContentPage(@RequestBody @Valid QueryHMCContentPageForm form) {
        if(Objects.nonNull(form.getStartTime())) {
            form.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        }
        if(Objects.nonNull(form.getEndTime())) {
            form.setEndTime(DateUtil.endOfDay(form.getEndTime()));
        }
        QueryHMCContentPageRequest request = PojoUtils.map(form, QueryHMCContentPageRequest.class);
        Page<HMCContentDTO> contentDTOPage = hmcContentApi.listPage(request);
        if (contentDTOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        List<Long> createUserIds = contentDTOPage.getRecords().stream().map(HMCContentDTO::getCreateUser).collect(Collectors.toList());
        List<Long> updateUserIds = contentDTOPage.getRecords().stream().map(HMCContentDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Integer> docIdList = contentDTOPage.getRecords().stream().map(item -> item.getDocId().intValue()).distinct().collect(Collectors.toList());
        List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(docIdList);
        Map<Integer, HmcDoctorInfoDTO> doctorInfoMap = doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));
        List<Long> userIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Page<HMCContentVO> contentVOPage = PojoUtils.map(contentDTOPage, HMCContentVO.class);
        contentVOPage.getRecords().forEach(contentVO -> {
            contentVO.setCreateUserName(userDTOMap.getOrDefault(contentVO.getCreateUser(), new UserDTO()).getName());
            if (Objects.nonNull(contentVO.getDocId()) && contentVO.getDocId() > 0) {
                contentVO.setDocName(Optional.ofNullable(doctorInfoMap.get(contentVO.getDocId().intValue())).map(HmcDoctorInfoDTO::getDoctorName).orElse(null));
            }
        });
        return Result.success(contentVOPage);
    }

}
