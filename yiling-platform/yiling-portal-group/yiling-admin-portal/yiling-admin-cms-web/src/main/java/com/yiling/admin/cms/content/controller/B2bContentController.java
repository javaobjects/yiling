package com.yiling.admin.cms.content.controller;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.content.form.AddHmcContentForm;
import com.yiling.admin.cms.content.form.ContentRankTopForm;
import com.yiling.admin.cms.content.form.ContentReferStatusForm;
import com.yiling.admin.cms.content.form.QueryHMCContentPageForm;
import com.yiling.admin.cms.content.vo.HMCContentVO;
import com.yiling.cms.content.api.B2bContentApi;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 大运河内容表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-06-25
 */
@Api(tags = "大运河内容管理")
@RestController
@RequestMapping("/b2b/content")
public class B2bContentController extends BaseController {
    @DubboReference
    private B2bContentApi b2bContentApi;

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
        b2bContentApi.addContent(request);
        return Result.success(true);
    }

    @ApiOperation("内容排序")
    @PostMapping("contentRank")
    public Result<Boolean> contentRank(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentRankTopForm form) {
        ContentRankRequest request = new ContentRankRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = b2bContentApi.contentRank(request);
        return Result.success(result);
    }

    @ApiOperation("修改引用")
    @PostMapping("updateReferStatus")
    public Result<Boolean> updateReferStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentReferStatusForm form) {
        ContentReferStatusRequest request = new ContentReferStatusRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = b2bContentApi.updateReferStatus(request);
        return Result.success(result);
    }

    @ApiOperation("列表")
    @PostMapping("queryContentPage")
    public Result<Page<HMCContentVO>> queryContentPage(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid QueryHMCContentPageForm form) {
        if (Objects.nonNull(form.getStartTime())) {
            form.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        }
        if (Objects.nonNull(form.getEndTime())) {
            form.setEndTime(DateUtil.endOfDay(form.getEndTime()));
        }
        QueryHMCContentPageRequest request = PojoUtils.map(form, QueryHMCContentPageRequest.class);
        Page<HMCContentDTO> contentDTOPage = b2bContentApi.listPage(request);
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
