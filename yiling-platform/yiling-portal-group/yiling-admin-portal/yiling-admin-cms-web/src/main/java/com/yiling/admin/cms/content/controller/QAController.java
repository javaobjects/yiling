package com.yiling.admin.cms.content.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.admin.cms.content.form.QueryQAPageForm;
import com.yiling.admin.cms.content.form.SwitchQaStatusForm;
import com.yiling.admin.cms.content.vo.ContentVO;
import com.yiling.admin.cms.content.vo.HmcDoctorInfoVO;
import com.yiling.admin.cms.content.vo.QaVO;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.api.QaApi;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.dto.request.SwitchQAStatusRequest;
import com.yiling.cms.content.enums.QaTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.HmcUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 医患问答
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-14
 */
@Api(tags = "医患问答")
@RestController
@RequestMapping("/qa")
@Slf4j
public class QAController extends BaseController {

    @DubboReference
    QaApi qaApi;

    @DubboReference
    ContentApi contentApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    DoctorApi doctorApi;

    @ApiOperation("QA列表")
    @GetMapping("queryQaPage")
    @Log(title = "QA列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<QaVO>> queryContentPage(@Valid QueryQAPageForm form) {
        QueryQAPageRequest queryQAPageRequest = PojoUtils.map(form, QueryQAPageRequest.class);
        if (StrUtil.isNotBlank(form.getTitle())) {
            List<ContentDTO> contentDTOList = contentApi.getByTitle(form.getTitle());
            if (Objects.isNull(contentDTOList) || CollUtil.isEmpty(contentDTOList)) {
                return Result.success(form.getPage());
            }

            List<Long> contentIdList = contentDTOList.stream().map(ContentDTO::getId).collect(Collectors.toList());
            queryQAPageRequest.setContentIdList(contentIdList);

        }
        Page<QaDTO> qaDTOPage = qaApi.listPage(queryQAPageRequest);
        if (Objects.isNull(qaDTOPage) || CollUtil.isEmpty(qaDTOPage.getRecords())) {
            return Result.success(form.getPage());
        }

        // C端用户信息
        List<Long> userIdList = qaDTOPage.getRecords().stream().filter(item -> QaTypeEnum.QUESTION.getCode().equals(item.getQaType())).map(QaDTO::getCreateUser).collect(Collectors.toList());
        Map<Long, HmcUser> userMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(userIdList)) {
            List<HmcUser> hmcUsers = hmcUserApi.listByIds(userIdList);
            userMap.putAll(hmcUsers.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity())));
        }

        // 医生信息
        List<Long> doctorIds = qaDTOPage.getRecords().stream().filter(item -> QaTypeEnum.ANSWER.getCode().equals(item.getQaType())).map(QaDTO::getCreateUser).collect(Collectors.toList());
        Map<Integer, HmcDoctorInfoDTO> doctorInfoDTOMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(doctorIds)) {
            List<Integer> doctorIdList = doctorIds.stream().map(Long::intValue).collect(Collectors.toList());
            List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(doctorIdList);
            if (CollUtil.isNotEmpty(doctorInfoList)) {
                doctorInfoDTOMap.putAll(doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity())));
            }
        }

        // 内容
        List<Long> contentIdList = qaDTOPage.getRecords().stream().map(QaDTO::getContentId).collect(Collectors.toList());
        List<ContentDTO> contentInfoByIdList = contentApi.getContentInfoByIdList(contentIdList);
        Map<Long, ContentDTO> contentDTOMap = contentInfoByIdList.stream().collect(Collectors.toMap(ContentDTO::getId, Function.identity()));

        Page<QaVO> result = PojoUtils.map(qaDTOPage, QaVO.class);
        result.getRecords().forEach(item -> {
            if (userMap.containsKey(item.getCreateUser())) {
                item.setNickName(userMap.get(item.getCreateUser()).getNickName());
                item.setMobile(userMap.get(item.getCreateUser()).getMobile());
            }
            if (QaTypeEnum.ANSWER.getCode().equals(item.getQaType()) && item.getCreateUser() > 0 && doctorInfoDTOMap.containsKey(item.getCreateUser().intValue())) {
                HmcDoctorInfoDTO doctorInfoDTO = doctorInfoDTOMap.get(item.getCreateUser().intValue());
                HmcDoctorInfoVO doctorInfoVO = PojoUtils.map(doctorInfoDTO, HmcDoctorInfoVO.class);
                item.setDoctorInfoVo(doctorInfoVO);
            }
            if (contentDTOMap.containsKey(item.getContentId())) {
                item.setContentTitle(contentDTOMap.get(item.getContentId()).getTitle());
            }
        });
        return Result.success(result);
    }

    @ApiOperation("切换状态")
    @PostMapping("switchShowStatus")
    @Log(title = "切换状态", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> switchShowStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid SwitchQaStatusForm form) {
        SwitchQAStatusRequest request = PojoUtils.map(form, SwitchQAStatusRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(qaApi.switchShowStatus(request));

    }


}
