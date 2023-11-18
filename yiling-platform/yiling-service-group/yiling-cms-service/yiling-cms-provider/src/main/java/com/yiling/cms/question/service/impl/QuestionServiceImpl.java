package com.yiling.cms.question.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionReplyDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionStandardGoodsInfoDTO;
import com.yiling.cms.question.dto.ReplyFileDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.QuestionStandardGoodsInfoRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;
import com.yiling.cms.question.entity.QuestionContentRelationDO;
import com.yiling.cms.question.entity.QuestionDO;
import com.yiling.cms.question.dao.QuestionMapper;
import com.yiling.cms.question.entity.QuestionReplyDO;
import com.yiling.cms.question.entity.QuestionReplyResourceDO;
import com.yiling.cms.question.entity.QuestionResourceDO;
import com.yiling.cms.question.enums.QuestionErrorCode;
import com.yiling.cms.question.service.QuestionContentRelationService;
import com.yiling.cms.question.service.QuestionReplyResourceService;
import com.yiling.cms.question.service.QuestionReplyService;
import com.yiling.cms.question.service.QuestionResourceService;
import com.yiling.cms.question.service.QuestionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 疑问处理库 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Service
public class QuestionServiceImpl extends BaseServiceImpl<QuestionMapper, QuestionDO> implements QuestionService {

    @Autowired
    private QuestionResourceService questionResourceService;
    @Autowired
    private QuestionReplyService questionReplyService;
    @Autowired
    private QuestionReplyResourceService questionReplyResourceService;
    @Autowired
    private QuestionContentRelationService questionContentRelationService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateQuestion(SaveQuestionRequest request) {
        QuestionDO question = PojoUtils.map(request, QuestionDO.class);
        saveOrUpdate(question);

        QuestionContentRelationDO contentRelationDO = new QuestionContentRelationDO();
        contentRelationDO.setOpUserId(request.getOpUserId());
        contentRelationDO.setOpTime(new Date());
        questionContentRelationService.batchDeleteWithFill(contentRelationDO, new LambdaQueryWrapper<QuestionContentRelationDO>().eq(QuestionContentRelationDO::getQuestionId, question.getId()));

        QuestionContentRelationDO saveContentRelationDO = new QuestionContentRelationDO();
        saveContentRelationDO.setQuestionId(question.getId());
        saveContentRelationDO.setContent(request.getContent());
        saveContentRelationDO.setOpUserId(request.getOpUserId());
        saveContentRelationDO.setOpTime(new Date());
        questionContentRelationService.save(saveContentRelationDO);

        //先删除在增加
        QuestionResourceDO resourceDO = new QuestionResourceDO();
        resourceDO.setOpUserId(request.getOpUserId());
        resourceDO.setOpTime(new Date());
        questionResourceService.batchDeleteWithFill(resourceDO, new LambdaQueryWrapper<QuestionResourceDO>().eq(QuestionResourceDO::getQuestionId, question.getId()));

        List<QuestionResourceDO> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(request.getDocumentIdList())) {
            for (Long documentId : request.getDocumentIdList()) {
                QuestionResourceDO one = new QuestionResourceDO();
                one.setQuestionId(question.getId());
                one.setType(1);
                one.setDocumentId(documentId);
                one.setOpTime(request.getOpTime());
                one.setOpUserId(request.getOpUserId());
                list.add(one);
            }
        }
        if (CollectionUtil.isNotEmpty(request.getStandardInfoList())) {
            for (QuestionStandardGoodsInfoRequest standardGoods : request.getStandardInfoList()) {
                QuestionResourceDO one = new QuestionResourceDO();
                one.setQuestionId(question.getId());
                one.setType(2);
                one.setStandardId(standardGoods.getStandardId());
                one.setSellSpecificationsId(standardGoods.getSellSpecificationsId());
                one.setOpTime(request.getOpTime());
                one.setOpUserId(request.getOpUserId());
                list.add(one);
            }
        }
        if (CollectionUtil.isNotEmpty(request.getKeyList())) {
            for (String key : request.getKeyList()) {
                QuestionResourceDO one = new QuestionResourceDO();
                one.setQuestionId(question.getId());
                one.setType(4);
                one.setResourceKey(key);
                one.setOpTime(request.getOpTime());
                one.setOpUserId(request.getOpUserId());
                list.add(one);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            questionResourceService.saveBatch(list);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteQuestion(Long questionId, Long OpUserId) {
        QuestionDO one = new QuestionDO();
        one.setId(questionId);
        one.setOpUserId(OpUserId);
        one.setOpTime(new Date());
        deleteByIdWithFill(one);

        QuestionContentRelationDO contentRelationDO = new QuestionContentRelationDO();
        contentRelationDO.setOpUserId(OpUserId);
        contentRelationDO.setOpTime(new Date());
        questionContentRelationService.batchDeleteWithFill(contentRelationDO, new LambdaQueryWrapper<QuestionContentRelationDO>().eq(QuestionContentRelationDO::getQuestionId, questionId));


        QuestionResourceDO resourceDO = new QuestionResourceDO();
        resourceDO.setOpUserId(OpUserId);
        resourceDO.setOpTime(new Date());
        questionResourceService.batchDeleteWithFill(resourceDO, new LambdaQueryWrapper<QuestionResourceDO>().eq(QuestionResourceDO::getQuestionId, questionId));



        List<QuestionReplyDO> questionReplyList = questionReplyService.listByQuestionId(questionId);
        if(CollectionUtil.isNotEmpty(questionReplyList)){
            List<Long> replyIdList = questionReplyList.stream().map(o -> o.getReplyId()).collect(Collectors.toList());
            QuestionReplyDO replyDO = new QuestionReplyDO();
            questionReplyService.batchDeleteWithFill(replyDO,new LambdaQueryWrapper<QuestionReplyDO>().eq(QuestionReplyDO::getQuestionId, questionId));

            QuestionReplyResourceDO replyResourceDO = new QuestionReplyResourceDO();
            replyResourceDO.setOpUserId(OpUserId);
            replyResourceDO.setOpTime(new Date());
            questionReplyResourceService.batchDeleteWithFill(replyResourceDO,new LambdaQueryWrapper<QuestionReplyResourceDO>().eq(QuestionReplyResourceDO::getReplyId, replyIdList));
        }
        return true;
    }

    @Override
    public Page<QuestionDO> listPage(QueryQuestionPageRequest request) {
        QueryWrapper<QuestionDO> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(request.getTitle())){
            wrapper.lambda().like(QuestionDO :: getTitle,request.getTitle());
        }
        if(request.getStartCreateTime() != null){
            wrapper.lambda().ge(QuestionDO :: getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if(request.getEndCreateTime() != null){
            wrapper.lambda().le(QuestionDO :: getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(request.getReplyFlag() != null && request.getReplyFlag() !=0 ){
            wrapper.lambda().eq(QuestionDO :: getReplyFlag,request.getReplyFlag());

        }
        if(request.getType()!=null && request.getType()!=0){
            wrapper.lambda().eq(QuestionDO :: getType,request.getType());
        }

        if(request.getToUserId() != null && request.getToUserId() != 0){
            wrapper.lambda().eq(QuestionDO :: getToUserId,request.getToUserId());
        }

        if(request.getFromUserId() != null && request.getFromUserId() != 0){
            wrapper.lambda().eq(QuestionDO :: getFromUserId,request.getFromUserId());
        }

        if(request.getStandardId() !=null && request.getStandardId()!=0 ){
            QueryWrapper<QuestionResourceDO> resourceWrapper = new QueryWrapper<>();
            resourceWrapper.lambda().eq(QuestionResourceDO :: getStandardId,request.getStandardId());
            if(request.getSellSpecificationsId()!=null && request.getSellSpecificationsId()!=0  ){
                resourceWrapper.lambda().eq(QuestionResourceDO :: getSellSpecificationsId,request.getSellSpecificationsId());
            }
            List<QuestionResourceDO> resourceList = questionResourceService.list(resourceWrapper);
            if(CollectionUtil.isNotEmpty(resourceList)){
                List<Long> ids = resourceList.stream().map(o -> o.getQuestionId()).collect(Collectors.toList());
                wrapper.lambda().in(QuestionDO :: getId,ids);
            }else{
                return new Page<>();
            }
        }

        wrapper.lambda().orderByDesc(QuestionDO :: getCreateTime);


        return this.baseMapper.selectPage(request.getPage(),wrapper);
    }

    @Override
    public QuestionDetailInfoDTO getQuestionDetail(Long questionId) {
        QuestionDO questionDO = getById(questionId);
        if(questionDO == null ){
           throw new BusinessException(QuestionErrorCode.QUESTION_NOT_EXIST);
        }
        QuestionDetailInfoDTO result = PojoUtils.map(questionDO, QuestionDetailInfoDTO.class);

        QuestionContentRelationDO questionContentRelation = questionContentRelationService.selectOneByQuestionId(questionId);
        if(questionContentRelation != null){
            result.setContent(questionContentRelation.getContent());
        }

        List<QuestionResourceDO> questionResourceList = questionResourceService.listByQuestionId(questionId);
        if(CollectionUtil.isNotEmpty(questionResourceList)){
            //文献集合
            List<Long> documentIdList = new ArrayList<>();
            //商品集合
            List<QuestionStandardGoodsInfoDTO> standardGoodsList = new ArrayList<>();
            //链接集合
            List<String> urlList = new ArrayList<>();
            //图片说明集合
            List<String> keyList = new ArrayList<>();
            for(QuestionResourceDO resourceDO : questionResourceList){
                if(resourceDO.getType() == 1){
                    documentIdList.add(resourceDO.getDocumentId());
                }else if (resourceDO.getType() == 2){
                    QuestionStandardGoodsInfoDTO standardGoods = new QuestionStandardGoodsInfoDTO();
                    standardGoods.setStandardId(resourceDO.getStandardId());
                    standardGoods.setSellSpecificationsId(resourceDO.getSellSpecificationsId());
                    standardGoodsList.add(standardGoods);
                }else if(resourceDO.getType() == 3){
                    urlList.add(resourceDO.getUrl());
                }else if (resourceDO.getType() == 4){
                    keyList.add(resourceDO.getResourceKey());
                }
            }
            result.setDocumentIdList(documentIdList);
            result.setStandardGoodsList(standardGoodsList);
            result.setUrlList(urlList);
            result.setKeyList(keyList);
        }
        List<QuestionReplyDO> questionReplyList = questionReplyService.listByQuestionId(questionId);
        List<QuestionReplyDetailInfoDTO> replyDetailList = PojoUtils.map(questionReplyList, QuestionReplyDetailInfoDTO.class);
        if(CollectionUtil.isNotEmpty(replyDetailList)){
            List<Long> replyIds = replyDetailList.stream().map(o -> o.getReplyId()).collect(Collectors.toList());
            List<QuestionReplyResourceDO> questionReplyResourceList = questionReplyResourceService.listByReplyIds(replyIds);
            Map<Long, List<QuestionReplyResourceDO>> replyMap = questionReplyResourceList.stream().collect(Collectors.groupingBy(QuestionReplyResourceDO::getReplyId));
            for(QuestionReplyDetailInfoDTO one : replyDetailList){
                List<QuestionReplyResourceDO> replyResourceList = replyMap.get(one.getReplyId());

                if(CollectionUtil.isNotEmpty(replyResourceList)){
                    //文献集合
                    List<Long> replyDocumentIdList = new ArrayList<>();
                    //图片集合
                    List<String>  replyPictureList = new ArrayList<>();
                    //链接集合
                    List<String> replyUrlList = new ArrayList<>();
                    //附件集合
                    List<ReplyFileDTO> replyFileKeyList = new ArrayList<>();

                    for(QuestionReplyResourceDO replyResourceDO : replyResourceList ){
                        if(replyResourceDO.getType() == 1){
                            replyDocumentIdList.add(replyResourceDO.getDocumentId());
                        }else if (replyResourceDO.getType() == 2){
                            replyUrlList.add(replyResourceDO.getUrl());
                        }else if(replyResourceDO.getType() == 3){
                            replyPictureList.add(replyResourceDO.getResourceKey());
                        }else if (replyResourceDO.getType() == 4){
                            ReplyFileDTO replyFile = new ReplyFileDTO();
                            replyFile.setReplyFileKey(replyResourceDO.getResourceKey());
                            replyFile.setFileName(replyResourceDO.getFileName());
                            replyFileKeyList.add(replyFile);
                        }
                    }
                    one.setReplyDocumentIdList(replyDocumentIdList);
                    one.setReplyPictureKeyList(replyPictureList);
                    one.setReplyUrlList(replyUrlList);
                    one.setReplyFileKeyList(replyFileKeyList);
                }
            }
        }
        result.setReplyDetailList(replyDetailList);
        return result;
    }

    @Override
    public Boolean addViewCount(Long questionId, Integer count) {
        QuestionDO question = getById(questionId);
        question.setViewCount(question.getViewCount() +count );
        return updateById(question);
    }

}
