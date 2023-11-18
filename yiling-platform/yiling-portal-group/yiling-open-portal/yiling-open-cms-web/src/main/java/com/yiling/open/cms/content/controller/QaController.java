package com.yiling.open.cms.content.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.cms.content.api.QaApi;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.AddQaRequest;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.enums.QaShowStatusEnum;
import com.yiling.cms.content.enums.QaSourceEnum;
import com.yiling.cms.content.enums.QaTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.cms.content.form.AddQaForm;
import com.yiling.open.cms.content.form.GetQaListForm;
import com.yiling.open.cms.content.vo.AnswerVO;
import com.yiling.open.cms.content.vo.QaVO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.HmcUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 医患问答
 *
 * @author: fan.shen
 * @date: 2023/3/13
 */
@Api(tags = "医患问答")
@RestController
@RequestMapping("/qa")
@Slf4j
public class QaController extends BaseController {

    @DubboReference
    QaApi qaApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @ApiOperation("01、添加回复")
    @PostMapping("addAnswer")
    @Log(title = "我要提问", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> addAnswer(@RequestBody @Valid AddQaForm form) {
        AddQaRequest qaRequest = PojoUtils.map(form, AddQaRequest.class);
        qaRequest.setOpUserId(form.getDoctorId());
        qaRequest.setShowStatus(QaShowStatusEnum.show.getCode());
        qaRequest.setLineId(LineEnum.IH_DOC.getCode());
        qaRequest.setQaType(QaTypeEnum.ANSWER.getCode());
        qaRequest.setQaSource(QaSourceEnum.CONTENT.getCode());
        return Result.success(qaApi.add(qaRequest));
    }

    @ApiOperation("02、获取QA列表")
    @PostMapping("getQaListByContentId")
    @Log(title = "我要提问", businessType = BusinessTypeEnum.OTHER)
    public Result<List<QaVO>> getQaListByContentId(@RequestBody @Valid GetQaListForm form) {
        List<QaDTO> qaDTOList = qaApi.getQaListByContentId(form.getContentId());
        if (CollUtil.isEmpty(qaDTOList)) {
            log.info("根据参数未获取到问答记录，参数:{}", JSONUtil.toJsonStr(form));
            return Result.success(Lists.newArrayList());
        }
        List<QaVO> result = Lists.newArrayList();
        List<Long> userIdList = qaDTOList.stream().map(QaDTO::getCreateUser).collect(Collectors.toList());
        List<HmcUser> hmcUsers = hmcUserApi.listByIds(userIdList);
        Map<Long, HmcUser> userMap = hmcUsers.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));

        Map<Long, List<QaDTO>> answerMap = qaDTOList.stream().filter(item -> QaTypeEnum.ANSWER.getCode().equals(item.getQaType())).collect(Collectors.groupingBy(QaDTO::getQaId));
        List<QaDTO> askDTOList = qaDTOList.stream().filter(item -> QaTypeEnum.QUESTION.getCode().equals(item.getQaType())).collect(Collectors.toList());
        askDTOList.forEach(askDTO -> {
            QaVO qaVO = PojoUtils.map(askDTO, QaVO.class);
            if (answerMap.containsKey(askDTO.getId())) {
                List<QaDTO> answerList = answerMap.get(askDTO.getId());
                qaVO.setAnswerList(PojoUtils.map(answerList, AnswerVO.class));
            }
            if (userMap.containsKey(askDTO.getCreateUser())) {
                qaVO.setNickName(userMap.get(askDTO.getCreateUser()).getNickName());
                String avatarUrl = userMap.get(askDTO.getCreateUser()).getAvatarUrl();
                if (StrUtil.isBlank(avatarUrl)) {
                    avatarUrl = Constants.HZ_DEFAULT_AVATAR;
                }
                qaVO.setAvatarUrl(avatarUrl);
            }
            result.add(qaVO);
        });

        return Result.success(result);
    }


}