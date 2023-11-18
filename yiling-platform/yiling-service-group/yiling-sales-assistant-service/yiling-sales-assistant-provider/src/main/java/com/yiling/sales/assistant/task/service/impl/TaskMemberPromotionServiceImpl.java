package com.yiling.sales.assistant.task.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.sales.assistant.task.dao.TaskMemberPromotionMapper;
import com.yiling.sales.assistant.task.dto.TaskMemberPromotiondDTO;
import com.yiling.sales.assistant.task.entity.TaskMemberPromotionDO;
import com.yiling.sales.assistant.task.service.TaskMemberPromotionService;

/**
 * <p>
 * 会员推广-买赠任务 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-12-16
 */
@Service
public class TaskMemberPromotionServiceImpl extends BaseServiceImpl<TaskMemberPromotionMapper, TaskMemberPromotionDO> implements TaskMemberPromotionService {

    @DubboReference
    private PromotionActivityApi promotionActivityApi;
    @DubboReference
    private GoodsGiftApi         goodsGiftApi;
    @Override
    public TaskMemberPromotiondDTO getMemberPromotion(Long taskId){
      /*  LambdaQueryWrapper<TaskMemberPromotionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberPromotionDO::getTaskId,taskId);
        TaskMemberPromotionDO memberPromotionDO = this.getOne(wrapper);
        TaskMemberPromotiondDTO taskMemberPromotiondDTO = new TaskMemberPromotiondDTO();
        PojoUtils.map(memberPromotionDO,taskMemberPromotiondDTO);
        PromotionActivityDTO promotionActivityDTO = promotionActivityApi.queryById(memberPromotionDO.getPromotionActivityId());
        taskMemberPromotiondDTO.setBeginTime(promotionActivityDTO.getBeginTime()).setEndTime(promotionActivityDTO.getEndTime())
                .setName(promotionActivityDTO.getName()).setPromotionActivityId(promotionActivityDTO.getId()).setPromotionAmount(promotionActivityDTO.getPromotionAmount());
        PromotionGoodsGiftLimitDTO promotionGoodsGiftLimitDTO = promotionActivityApi.queryGoodsGiftByActivityId(promotionActivityDTO.getId());
        GoodsGiftDTO goodsGiftDTO = goodsGiftApi.getOneById(promotionGoodsGiftLimitDTO.getGoodsGiftId());
        taskMemberPromotiondDTO.setGiftName(goodsGiftDTO.getName());*/
        return null;
    }
}
