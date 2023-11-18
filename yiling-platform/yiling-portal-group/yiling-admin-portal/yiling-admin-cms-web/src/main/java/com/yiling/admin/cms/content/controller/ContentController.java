package com.yiling.admin.cms.content.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.admin.cms.content.form.*;
import com.yiling.admin.cms.content.vo.*;
import com.yiling.admin.cms.goods.vo.GoodsInfoVO;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.api.HMCContentApi;
import com.yiling.cms.content.api.IHDocContentApi;
import com.yiling.cms.content.api.IHPatientContentApi;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.dto.request.AddContentRequest;
import com.yiling.cms.content.dto.request.DeleteDraftRequest;
import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentRequest;
import com.yiling.cms.content.enums.CreateSourceEnum;
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
import com.yiling.framework.common.util.JsonUtil;
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
import com.yiling.ih.user.api.IHUserApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.ih.user.dto.IHUserDTO;
import com.yiling.ih.user.dto.request.QueryDoctorPageRequest;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * 内容 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Api(tags = "内容")
@RestController
@RequestMapping("/cms/content")
@Slf4j
public class ContentController extends BaseController {

    @DubboReference
    private ContentApi contentApi;

    @DubboReference
    private HMCContentApi hmcContentApi;

    @DubboReference
    private IHDocContentApi ihDocContentApi;

    @DubboReference
    private IHPatientContentApi ihPatientContentApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private StaffApi staffApi;

    @DubboReference
    private IHUserApi ihUserApi;

    @DubboReference
    private StandardGoodsApi standardGoodsApi;

    @DubboReference
    private MeetingApi meetingApi;

    @DubboReference
    private DiseaseApi diseaseApi;

    @DubboReference
    private HospitalDeptApi hospitalDeptApi;

    @DubboReference
    private AdminApi adminApi;

    @Autowired
    private FileService fileService;

    @DubboReference
    DoctorApi doctorApi;

    @Log(title = "添加内容", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加")
    @PostMapping("addContent")
    public Result<Boolean> addContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddContentForm addContentForm) {
        AddContentRequest request = new AddContentRequest();
        if (addContentForm.getIsDraft() == 0) {
            if (StrUtil.isEmpty(addContentForm.getContent()) && 1 == addContentForm.getSourceContentType()) {
                throw new BusinessException(ResultCode.FAILED, "请输入内容详情");
            }
            if (2 == addContentForm.getSourceContentType() && StrUtil.isEmpty(addContentForm.getLinkUrl())) {
                throw new BusinessException(ResultCode.FAILED, "请输入H5地址");
            }
        }
        PojoUtils.map(addContentForm, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        contentApi.addContent(request);
        return Result.success(true);
    }

    @Log(title = "编辑内容", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑")
    @PostMapping("updateContent")
    public Result<Boolean> updateContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateContentForm updateContentForm) {
        UpdateContentRequest request = new UpdateContentRequest();
        PojoUtils.map(updateContentForm, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        contentApi.updateContent(request);
        return Result.success(true);
    }

    @ApiOperation("列表")
    @GetMapping("queryContentPage")
    @Log(title = "查询文章列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ContentVO>> queryContentPage(@Valid QueryContentPageForm form) {
        form.checkParam();

        QueryContentPageRequest request = new QueryContentPageRequest();

        // 判断创建来源 & 创建人名称是否为空
        if (StrUtil.isNotBlank(form.getCreateUserName())) {
            // 运营后台
            if (CreateSourceEnum.ADMIN.getCode().equals(form.getCreateSource())) {
                List<Admin> adminList = adminApi.getListByName(form.getCreateUserName(), 10);
                if (CollUtil.isEmpty(adminList)) {
                    return Result.success(form.getPage());
                }
                request.setCreateUserIdList(adminList.stream().map(Admin::getId).collect(Collectors.toList()));
            }

            // IH 运营后台
            if (CreateSourceEnum.IH_ADMIN.getCode().equals(form.getCreateSource())) {
                List<IHUserDTO> ihUserDTOList = ihUserApi.getUserListByName(form.getCreateUserName());
                if (CollUtil.isEmpty(ihUserDTOList)) {
                    return Result.success(form.getPage());
                }
                request.setCreateUserIdList(ihUserDTOList.stream().map(item -> Long.valueOf(item.getId())).collect(Collectors.toList()));
            }

        }
        PojoUtils.map(form, request);
        log.info("[queryContentPage]入参:{}", JSONUtil.toJsonStr(request));
        Page<ContentDTO> contentDTOPage = contentApi.listPage(request);
        if (contentDTOPage.getTotal() == 0) {
            log.warn("contentApi.listPage返回结果为空");
            return Result.success(form.getPage());
        }
        List<Long> createUserIds = contentDTOPage.getRecords().stream().map(ContentDTO::getCreateUser).collect(Collectors.toList());
        List<Long> updateUserIds = contentDTOPage.getRecords().stream().map(ContentDTO::getUpdateUser).distinct().collect(Collectors.toList());

        List<Integer> docIdList = contentDTOPage.getRecords().stream().map(item -> item.getDocId().intValue()).distinct().collect(Collectors.toList());

        List<Integer> ihCreateUserIdList = contentDTOPage.getRecords().stream().filter(item -> CreateSourceEnum.IH_ADMIN.getCode().equals(item.getCreateSource()))
                .map(item -> item.getCreateUser().intValue()).distinct().collect(Collectors.toList());

        List<Integer> ihUpdateUserIdList = contentDTOPage.getRecords().stream().filter(item -> CreateSourceEnum.IH_ADMIN.getCode().equals(item.getCreateSource()))
                .map(item -> item.getUpdateUser().intValue()).distinct().collect(Collectors.toList());


        // List<Long> contentIdList = contentDTOPage.getRecords().stream().map(ContentDTO::getId).collect(Collectors.toList());

        // 声明容器 ， 用来判断文章是否被引用过
        // Map<Long, HMCContentDTO> hmcContentDTOMap = Maps.newHashMap();
        // Map<Long, IHDocContentDTO> ihDocContentDTOMap = Maps.newHashMap();
        // Map<Long, IHPatientContentDTO> ihPatientContentDTOMap = Maps.newHashMap();
        // if (Objects.nonNull(form.getLineId()) && LineEnum.HMC.getCode().equals(form.getLineId().intValue())) {
        //     List<HMCContentDTO> hmcContentDTOList = hmcContentApi.getContentByContentIdList(contentIdList);
        //     hmcContentDTOMap.putAll(hmcContentDTOList.stream().collect(Collectors.toMap(HMCContentDTO::getContentId, o -> o, (k1, k2) -> k1)));
        // }
        //
        // if (Objects.nonNull(form.getLineId()) && LineEnum.IH_DOC.getCode().equals(form.getLineId().intValue())) {
        //     List<IHDocContentDTO> ihDocContentDTOList = ihDocContentApi.getContentByContentIdList(contentIdList);
        //     ihDocContentDTOMap.putAll(ihDocContentDTOList.stream().collect(Collectors.toMap(IHDocContentDTO::getContentId, o -> o, (k1, k2) -> k1)));
        // }
        //
        // if (Objects.nonNull(form.getLineId()) && LineEnum.IH_PATIENT.getCode().equals(form.getLineId().intValue())) {
        //     List<IHPatientContentDTO> ihPatientContentDTOList = ihPatientContentApi.getContentByContentIdList(contentIdList);
        //     ihPatientContentDTOMap.putAll(ihPatientContentDTOList.stream().collect(Collectors.toMap(IHPatientContentDTO::getContentId, o -> o, (k1, k2) -> k1)));
        // }

        List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(docIdList);
        Map<Integer, HmcDoctorInfoDTO> doctorInfoMap = doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));

        List<Long> userIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 获取IH 用户信息
        Map<Integer, IHUserDTO> ihUserDTOMap = Maps.newHashMap();
        List<Integer> ihUserIdList = Lists.newArrayList();
        ihUserIdList.addAll(ihCreateUserIdList);
        ihUserIdList.addAll(ihUpdateUserIdList);

        if (CollUtil.isNotEmpty(ihUserIdList)) {
            List<IHUserDTO> ihUserDTOList = ihUserApi.getUserListByIds(ihUserIdList);
            ihUserDTOMap.putAll(ihUserDTOList.stream().collect(Collectors.toMap(IHUserDTO::getId, Function.identity())));
        }

        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        contentVOPage.getRecords().forEach(contentVO -> {

            if (StrUtil.isNotEmpty(contentVO.getCover())) {
                contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
            }
            if (StrUtil.isNotEmpty(contentVO.getVedioFileUrl())) {
                contentVO.setVedioFileUrl(fileService.getUrl(contentVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
            }
            if (Objects.nonNull(contentVO.getDocId()) && contentVO.getDocId() > 0) {
                contentVO.setDocName(Optional.ofNullable(doctorInfoMap.get(contentVO.getDocId().intValue())).map(HmcDoctorInfoDTO::getDoctorName).orElse(Constants.SEPARATOR_MIDDLELINE));
            }

            if (CreateSourceEnum.IH_ADMIN.getCode().equals(contentVO.getCreateSource())) {
                contentVO.setCreateUserName(Optional.ofNullable(ihUserDTOMap.get(contentVO.getCreateUser().intValue())).map(IHUserDTO::getName).orElse(Constants.SEPARATOR_MIDDLELINE));
                contentVO.setUpdateUserName(Optional.ofNullable(ihUserDTOMap.get(contentVO.getUpdateUser().intValue())).map(IHUserDTO::getName).orElse(Constants.SEPARATOR_MIDDLELINE));
            } else {
                contentVO.setCreateUserName(userDTOMap.getOrDefault(contentVO.getCreateUser(), new UserDTO()).getName());
                contentVO.setUpdateUserName(userDTOMap.getOrDefault(contentVO.getUpdateUser(), new UserDTO()).getName());
            }

        });
        log.info("[queryContentPage]返回参数:{}", JSONUtil.toJsonStr(contentVOPage));
        return Result.success(contentVOPage);
    }

    @ApiOperation("详情")
    @GetMapping("getContentById")
    public Result<ContentVO> getContentById(@RequestParam Long id) {
        ContentDTO content = contentApi.getContentById(id);
        ContentVO contentVO = PojoUtils.map(content, ContentVO.class);
        contentVO.setCoverKey(contentVO.getCover());
        if (StrUtil.isNotEmpty(contentVO.getCover())) {
            contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
        }
        if (StringUtils.isNotEmpty(contentVO.getVedioFileUrl())) {
            contentVO.setVedioFileUrlKey(contentVO.getVedioFileUrl());
            contentVO.setVedioFileUrl(fileService.getUrl(contentVO.getVedioFileUrlKey(), FileTypeEnum.VEDIO_CONTENT));
        }
        List<Long> standardGoodsIdList = content.getStandardGoodsIdList();
        if (CollUtil.isNotEmpty(standardGoodsIdList)) {
            List<GoodsInfoVO> standardGoodsList = Lists.newArrayListWithExpectedSize(standardGoodsIdList.size());
            standardGoodsIdList.forEach(standardGoodsId -> {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                goodsInfoVO.setId(standardGoodsId);
                StandardGoodsAllInfoDTO standardGoods = standardGoodsApi.getStandardGoodsById(standardGoodsId);
                goodsInfoVO.setName(Optional.ofNullable(standardGoods).get().getBaseInfo().getName());
                standardGoodsList.add(goodsInfoVO);
            });
            contentVO.setStandardGoodsList(standardGoodsList);
        }

        //关联疾病
        if (CollUtil.isNotEmpty(content.getDiseaseIdList())) {
            QueryDiseaseListRequest request = new QueryDiseaseListRequest();
            request.setIdList(content.getDiseaseIdList());
            request.setSize(100);
            Page<DiseaseDTO> diseaseDTOPage = diseaseApi.queryDisease(request);
            List<DiseaseVO> diseaseVOList = PojoUtils.map(diseaseDTOPage.getRecords(), DiseaseVO.class);
            contentVO.setDiseaseVOList(diseaseVOList);
        }
        //关联科室
        if (CollUtil.isNotEmpty(content.getDeptIdList())) {
            QueryHospitalDeptListRequest request = new QueryHospitalDeptListRequest();
            request.setIds(content.getDeptIdList());
            List<HospitalDeptListDTO> hospitalDeptListDTOS = hospitalDeptApi.listByIds(request);
            List<HospitalDeptVO> hospitalDeptVOS = PojoUtils.map(hospitalDeptListDTOS, HospitalDeptVO.class);
            contentVO.setHospitalDeptVOS(hospitalDeptVOS);
        }

        // 获取医生名称
        if (Objects.nonNull(content.getDocId()) && content.getDocId() > 0) {
            DoctorAppInfoDTO doctorInfo = doctorApi.getDoctorInfoByDoctorId(content.getDocId().intValue());
            contentVO.setDocName(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getDoctorName).orElse(null));
            contentVO.setHospitalName(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getHospitalName).orElse(null));
            contentVO.setHospitalDepartment(Optional.ofNullable(doctorInfo).map(DoctorAppInfoDTO::getHospitalDepartment).orElse(null));
        }
        return Result.success(contentVO);
    }

    @ApiOperation("删除草稿")
    @PostMapping("deleteDraft")
    public Result<Boolean> deleteDraft(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody DeleteDraftForm form) {
        DeleteDraftRequest request = new DeleteDraftRequest();
        request.setId(form.getId());
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        contentApi.deleteDraft(request);
        return Result.success(true);
    }

    @ApiOperation("草稿")
    @GetMapping("queryDraftList")
    public Result<CollectionObject<List<DraftVO>>> queryDraftList(@CurrentUser CurrentAdminInfo currentAdminInfo) {
        BaseRequest request = new BaseRequest();
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        List<DraftDTO> draftDTOList = contentApi.queryDraftList(request);
        List<DraftVO> draftVOS = PojoUtils.map(draftDTOList, DraftVO.class);
        CollectionObject<List<DraftVO>> collectionObject = new CollectionObject(draftVOS);
        return Result.success(collectionObject);
    }

    @ApiOperation("选择关联会议分页列表")
    @PostMapping("queryMeetingPage")
    public Result<Page<MeetingVO>> queryMeetingPage(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid QueryMeetingPageForm form) {
        QueryMeetingPageListRequest request = PojoUtils.map(form, QueryMeetingPageListRequest.class);
        request.setShowStatus(MeetingShowStatusEnum.FINISHED.getCode());
        Page<MeetingDTO> meetingListPage = meetingApi.queryMeetingListPage(request);

        return Result.success(PojoUtils.map(meetingListPage, MeetingVO.class));
    }

    @ApiOperation("选择关联疾病")
    @GetMapping("queryDiseaseList")
    public Result<Page<DiseaseVO>> queryDiseaseList(@Valid QueryDiseasePageForm queryDiseasePageForm) {
        QueryDiseaseListRequest request = new QueryDiseaseListRequest();
        request.setName(queryDiseasePageForm.getDiseaseName());
        PojoUtils.map(queryDiseasePageForm, request);
        Page<DiseaseDTO> diseaseDTOPage = diseaseApi.queryDisease(request);
        Page<DiseaseVO> diseaseVOPage = PojoUtils.map(diseaseDTOPage, DiseaseVO.class);
        return Result.success(diseaseVOPage);
    }

    @ApiOperation("选择关联科室")
    @GetMapping("queryDepartmentList")
    public Result<CollectionObject<List<HospitalDeptVO>>> queryDepartmentList() {
        List<HospitalDeptDTO> departmentList = hospitalDeptApi.getDepartmentList();
        List<HospitalDeptVO> hospitalDeptVOS = PojoUtils.map(departmentList, HospitalDeptVO.class);
        CollectionObject<List<HospitalDeptVO>> collectionObject = new CollectionObject(hospitalDeptVOS);
        return Result.success(collectionObject);
    }

    @ApiOperation("搜索医生")
    @PostMapping("queryDoctorByNameAndHospital")
    @Log(title = "搜索医生", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<DoctorVO>> queryDoctorByNameAndHospital(@RequestBody @Valid QueryDoctorPageForm form) {
        QueryDoctorPageRequest request = PojoUtils.map(form, QueryDoctorPageRequest.class);
        Page<HmcDoctorInfoDTO> doctorInfoPage = doctorApi.queryDoctorPage(request);
        Page<DoctorVO> doctorVOPage = PojoUtils.map(doctorInfoPage, DoctorVO.class);
        return Result.success(doctorVOPage);
    }
    //
    // @ApiOperation("患者端/医生端内容列表")
    // @PostMapping("clientContentPage")
    // public Result<Page<ClientContentVO>> clientContentPage(@RequestBody @Valid QueryClientContentPageForm queryContentPageForm) {
    //     QueryContentPageRequest request = new QueryContentPageRequest();
    //     PojoUtils.map(queryContentPageForm, request);
    //     Page<ContentBO> contentDTOPage = contentApi.clientContentPage(request);
    //     if (contentDTOPage.getTotal() == 0) {
    //         return Result.success(queryContentPageForm.getPage());
    //     }
    //     List<Integer> docIdList = contentDTOPage.getRecords().stream().map(item -> item.getDocId().intValue()).distinct().collect(Collectors.toList());
    //     List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(docIdList);
    //     Map<Integer, HmcDoctorInfoDTO> doctorInfoMap = doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));
    //     Page<ClientContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ClientContentVO.class);
    //     contentVOPage.getRecords().forEach(contentVO -> {
    //         if (Objects.nonNull(contentVO.getDocId()) && contentVO.getDocId() > 0) {
    //             contentVO.setDocName(Optional.ofNullable(doctorInfoMap.get(contentVO.getDocId().intValue())).map(HmcDoctorInfoDTO::getDoctorName).orElse(null));
    //         }
    //     });
    //     return Result.success(contentVOPage);
    // }

    // @ApiOperation("内容排序")
    // @PostMapping("contentRank")
    // public Result<Boolean> contentRank(@RequestBody @Valid ContentRankTopForm form) {
    //     ContentRankRequest request = new ContentRankRequest();
    //     PojoUtils.map(form, request);
    //     Boolean result = contentApi.contentRank(request);
    //     return Result.success(result);
    // }
    //
    // @ApiOperation("内容置顶/取消置顶")
    // @PostMapping("contentTop")
    // public Result<Boolean> contentTop(@RequestBody @Valid ContentRankTopForm form) {
    //     ContentRankRequest request = new ContentRankRequest();
    //     PojoUtils.map(form, request);
    //     Boolean result = contentApi.contentTop(request);
    //     return Result.success(result);
    // }


}
