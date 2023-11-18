package com.yiling.goods.standard.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.CreateStandardTagRequest;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.SaveStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.UpdateStandardTagRequest;
import com.yiling.goods.standard.service.StandardGoodsTagRelService;
import com.yiling.goods.standard.service.StandardGoodsTagService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @author shichen
 * @类名 StandardGoodsTagApiImpl
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@DubboService
public class StandardGoodsTagApiImpl implements StandardGoodsTagApi {

    @Autowired
    private StandardGoodsTagService standardGoodsTagService;
    @Autowired
    private StandardGoodsTagRelService standardGoodsTagRelService;

    @Autowired
    @Lazy
    StandardGoodsTagApiImpl _this;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public List<StandardGoodsTagDTO> listAll(EnableStatusEnum statusEnum) {
        Assert.notNull(statusEnum, "参数statusEnum不能为空");
        return standardGoodsTagService.listAll(statusEnum);
    }

    @Override
    public List<StandardGoodsTagDTO> listByStandardId(Long standardId) {
        Assert.notNull(standardId, "参数StandardId不能为空");
        return standardGoodsTagService.listByStandardId(standardId);
    }

    @Override
    public Map<Long, List<StandardGoodsTagDTO>> listByStandardIdList(List<Long> standardIdList) {
        Assert.notEmpty(standardIdList, "参数eidList不能为空");
        return standardGoodsTagService.listByStandardIdList(standardIdList);
    }

    @Override
    public List<Long> getStandardIdListByTagId(Long tagId) {
        return standardGoodsTagRelService.getStandardIdListByTagId(tagId);
    }

    @Override
    public List<Long> getStandardIdListByTagIdList(List<Long> tagIdList) {
        return standardGoodsTagRelService.getStandardIdListByTagIdList(tagIdList);
    }

    @Override
    public boolean saveStandardGoodsTags(SaveStandardGoodsTagsRequest request) {
        Assert.notNull(request, "参数request不能为空");
        boolean b = standardGoodsTagRelService.saveStandardGoodsTags(request);
        if(CollectionUtil.isNotEmpty(request.getTagIds())&& null!= request.getStandardId()){
            StandardGoodsTagDTO tag = standardGoodsTagService.getTagByTagsName("2C平台药房");
            if(null!=tag && request.getTagIds().contains(tag.getId())){
                _this.sendMq(Constants.TOPIC_HMC_UPDATE_GOODS_TAG,Constants.TAG_HMC_UPDATE_GOODS_TAG,String.valueOf(request.getStandardId()),request.getStandardId());
            }
        }
        return b;
    }

    @Override
    public Boolean createTags(CreateStandardTagRequest request) {
        return standardGoodsTagService.createTags(request);
    }

    @Override
    public Boolean updateTags(UpdateStandardTagRequest request) {
        return standardGoodsTagService.updateTags(request);
    }

    @Override
    public Boolean batchDeleteTags(List<Long> tagsIdList, Long currentUserId) {
        Assert.notNull(tagsIdList, "参数tagsIdList不能为空");
        return standardGoodsTagService.batchDeleteTags(tagsIdList, currentUserId);
    }

    @Override
    public Page<StandardGoodsTagDTO> queryTagsListPage(QueryStandardGoodsTagsRequest request) {
        return standardGoodsTagService.queryTagsListPage(request);
    }

    @Override
    public List<Long> getStandardIdListByTagsName(String name) {
        return standardGoodsTagService.getStandardIdListByTagsName(name);
    }

    @Override
    public List<Long> getStandardIdListByTagsNameList(List<String> tagNameList) {
        return standardGoodsTagService.getStandardIdListByTagsNameList(tagNameList);
    }

    @Override
    public StandardGoodsTagDTO getTagByTagsName(String name) {
        return standardGoodsTagService.getTagByTagsName(name);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg,Long id) {
        Integer intId=null;
        if(null !=id && 0<id){
            intId=id.intValue();
        }
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg, intId);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg,Integer id) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg,id);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
