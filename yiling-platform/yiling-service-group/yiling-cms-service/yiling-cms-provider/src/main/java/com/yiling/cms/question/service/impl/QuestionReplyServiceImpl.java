package com.yiling.cms.question.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.cms.question.dao.QuestionReplyMapper;
import com.yiling.cms.question.dto.QuestionReplySendMessageDTO;
import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;
import com.yiling.cms.question.dto.request.SaveReplyFileRequest;
import com.yiling.cms.question.entity.QuestionDO;
import com.yiling.cms.question.entity.QuestionReplyDO;
import com.yiling.cms.question.entity.QuestionReplyResourceDO;
import com.yiling.cms.question.enums.QuestionErrorCode;
import com.yiling.cms.question.service.QuestionReplyResourceService;
import com.yiling.cms.question.service.QuestionReplyService;
import com.yiling.cms.question.service.QuestionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <p>
 * 医药代表回复表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Service
public class QuestionReplyServiceImpl extends BaseServiceImpl<QuestionReplyMapper, QuestionReplyDO> implements QuestionReplyService {
    @Autowired
    QuestionReplyResourceService questionReplyResourceService;
    @Autowired
    QuestionService questionService;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public List<QuestionReplyDO> listByQuestionId(Long questionId) {
        QueryWrapper<QuestionReplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionReplyDO::getQuestionId, questionId);
        return list(wrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveQuestionReply(SaveQuestionReplyRequest replyRequest) {
        QuestionDO questionDO = questionService.getById(replyRequest.getQuestionId());
        if (questionDO == null) {
            throw new BusinessException(QuestionErrorCode.QUESTION_PARAMETER_ERROR);
        }
        if(questionDO.getType() == 1){
            throw new BusinessException(QuestionErrorCode.QUESTION_PARAMETER_ERROR);
        }
        QuestionReplyDO replyDO = PojoUtils.map(replyRequest, QuestionReplyDO.class);
        replyDO.setCreateUser(replyRequest.getOpUserId());
        save(replyDO);

        QuestionReplyDO replyOne = new QuestionReplyDO();
        replyOne.setReplyId(replyDO.getId());
        replyOne.setId(replyDO.getId());
        updateById(replyOne);

        List<QuestionReplyResourceDO> replyResourceList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(replyRequest.getReplyDocumentIdList())) {
            for (Long documentId : replyRequest.getReplyDocumentIdList()) {
                QuestionReplyResourceDO questionReplyResourceDO = new QuestionReplyResourceDO();
                questionReplyResourceDO.setReplyId(replyDO.getId());
                questionReplyResourceDO.setType(1);
                questionReplyResourceDO.setDocumentId(documentId);
                questionReplyResourceDO.setOpUserId(replyRequest.getOpUserId());
                questionReplyResourceDO.setOpTime(replyRequest.getOpTime());
                replyResourceList.add(questionReplyResourceDO);
            }
        }

        if (CollectionUtil.isNotEmpty(replyRequest.getReplyFileKeyList())) {
            for (SaveReplyFileRequest fileKey : replyRequest.getReplyFileKeyList()) {
                QuestionReplyResourceDO questionReplyResourceDO = new QuestionReplyResourceDO();
                questionReplyResourceDO.setReplyId(replyDO.getId());
                questionReplyResourceDO.setType(4);
                questionReplyResourceDO.setResourceKey(fileKey.getReplyFileKey());
                questionReplyResourceDO.setFileName(fileKey.getFileName());
                questionReplyResourceDO.setOpUserId(replyRequest.getOpUserId());
                questionReplyResourceDO.setOpTime(replyRequest.getOpTime());
                replyResourceList.add(questionReplyResourceDO);
            }
        }

        if (CollectionUtil.isNotEmpty(replyRequest.getReplyPictureList())) {
            for (String pictureKey : replyRequest.getReplyPictureList()) {
                QuestionReplyResourceDO questionReplyResourceDO = new QuestionReplyResourceDO();
                questionReplyResourceDO.setReplyId(replyDO.getId());
                questionReplyResourceDO.setType(3);
                questionReplyResourceDO.setResourceKey(pictureKey);
                questionReplyResourceDO.setOpUserId(replyRequest.getOpUserId());
                questionReplyResourceDO.setOpTime(replyRequest.getOpTime());
                replyResourceList.add(questionReplyResourceDO);
            }
        }

        if (CollectionUtil.isNotEmpty(replyRequest.getReplyUrlList())) {
            for (String url : replyRequest.getReplyUrlList()) {
                QuestionReplyResourceDO questionReplyResourceDO = new QuestionReplyResourceDO();
                questionReplyResourceDO.setReplyId(replyDO.getId());
                questionReplyResourceDO.setType(2);
                questionReplyResourceDO.setUrl(url);
                questionReplyResourceDO.setOpUserId(replyRequest.getOpUserId());
                questionReplyResourceDO.setOpTime(replyRequest.getOpTime());
                replyResourceList.add(questionReplyResourceDO);
            }
        }

        if (CollectionUtil.isNotEmpty(replyResourceList)) {
            questionReplyResourceService.saveBatch(replyResourceList);
        }

        Boolean sendFlag = false;

        if (questionDO.getReplyFlag() == 1) {
            questionDO.setReplyFlag(2);
            questionDO.setLastReplyTime(new Date());
            sendFlag = true;
        } else {
            questionDO.setLastReplyTime(new Date());
        }
        questionService.updateById(questionDO);
        if (sendFlag && questionDO.getFromUserId() != null && questionDO.getFromUserId() != 0) {
            //第一次回复发送消息
            QuestionReplySendMessageDTO message = new QuestionReplySendMessageDTO();
            message.setDoctorId(questionDO.getFromUserId());
            message.setQuestionId(questionDO.getId());
            message.setTime(new Date());
            message.setTitle(questionDO.getTitle());
            MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_CMS_FIRST_REPLY_SEND, Constants.TAG_CMS_FIRST_REPLY_SEND, JSON.toJSONString(message));
            mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
            mqMessageSendApi.send(mqMessageTwoBO);

        }
        return true;
    }

    @Override
    public QuestionReplyDO selectLastReply(Long questionId) {
        QueryWrapper<QuestionReplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionReplyDO::getQuestionId, questionId).orderByDesc(QuestionReplyDO::getCreateTime).last("limit 1 ");
        return getOne(wrapper);
    }
}
