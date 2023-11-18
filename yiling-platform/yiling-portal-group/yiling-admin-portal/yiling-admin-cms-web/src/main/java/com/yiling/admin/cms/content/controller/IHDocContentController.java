package com.yiling.admin.cms.content.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.content.form.*;
import com.yiling.admin.cms.content.vo.IHDocContentVO;
import com.yiling.cms.content.api.IHDocContentApi;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.dto.request.*;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * IHDoc 前端控制器
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-08
 */
@Api(tags = "IHDoc内容管理")
@RestController
@RequestMapping("/ihDoc/content")
public class IHDocContentController extends BaseController {

    @DubboReference
    private IHDocContentApi ihDocContentApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    DoctorApi doctorApi;

    @Log(title = "添加内容", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加")
    @PostMapping("addContent")
    public Result<Boolean> addContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddIHDocContentForm form) {
        AddIHDocContentRequest request = PojoUtils.map(form, AddIHDocContentRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        ihDocContentApi.addContent(request);
        return Result.success(true);
    }

    @ApiOperation("内容排序")
    @PostMapping("contentRank")
    public Result<Boolean> contentRank(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentRankTopForm form) {
        ContentRankRequest request = new ContentRankRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = ihDocContentApi.contentRank(request);
        return Result.success(result);
    }

    @ApiOperation("修改引用")
    @PostMapping("updateReferStatus")
    public Result<Boolean> updateReferStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid ContentReferStatusForm form) {
        ContentReferStatusRequest request = new ContentReferStatusRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = ihDocContentApi.updateReferStatus(request);
        return Result.success(result);
    }

    @ApiOperation("获取IHDoc文章详情")
    @PostMapping("getIHDocContentDetailById")
    public Result<IHDocContentVO> getIHDocContentById(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid QueryIHDocContentForm form) {
        IHDocContentDTO ihDocContentDTO = ihDocContentApi.getIhDocContentById(form.getId());
        return Result.success(PojoUtils.map(ihDocContentDTO, IHDocContentVO.class));
    }

    @ApiOperation("更新IHDoc文章权限")
    @PostMapping("updateIHDocContentAuth")
    public Result<Boolean> updateIHDocContentAuth(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateIHDocContentAuthForm form) {
        UpdateContentAuthRequest request = PojoUtils.map(form, UpdateContentAuthRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean result = ihDocContentApi.updateIHDocContentAuth(request);
        return Result.success(result);
    }

    @ApiOperation("列表")
    @PostMapping("queryContentPage")
    public Result<Page<IHDocContentVO>> queryContentPage(@RequestBody @Valid QueryIHDocContentPageForm form) {
        if (Objects.nonNull(form.getStartTime())) {
            form.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        }
        if (Objects.nonNull(form.getEndTime())) {
            form.setEndTime(DateUtil.endOfDay(form.getEndTime()));
        }
        QueryIHDocContentPageRequest request = PojoUtils.map(form, QueryIHDocContentPageRequest.class);
        Page<IHDocContentDTO> contentDTOPage = ihDocContentApi.listPage(request);
        if (contentDTOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        List<Long> createUserIds = contentDTOPage.getRecords().stream().map(IHDocContentDTO::getCreateUser).collect(Collectors.toList());
        List<Long> updateUserIds = contentDTOPage.getRecords().stream().map(IHDocContentDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Integer> docIdList = contentDTOPage.getRecords().stream().map(item -> item.getDocId().intValue()).distinct().collect(Collectors.toList());
        List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(docIdList);
        Map<Integer, HmcDoctorInfoDTO> doctorInfoMap = doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));
        List<Long> userIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Page<IHDocContentVO> contentVOPage = PojoUtils.map(contentDTOPage, IHDocContentVO.class);
        contentVOPage.getRecords().forEach(contentVO -> {
            contentVO.setCreateUserName(userDTOMap.getOrDefault(contentVO.getCreateUser(), new UserDTO()).getName());
            if (Objects.nonNull(contentVO.getDocId()) && contentVO.getDocId() > 0) {
                contentVO.setDocName(Optional.ofNullable(doctorInfoMap.get(contentVO.getDocId().intValue())).map(HmcDoctorInfoDTO::getDoctorName).orElse(null));
            }
        });
        return Result.success(contentVOPage);
    }

}
