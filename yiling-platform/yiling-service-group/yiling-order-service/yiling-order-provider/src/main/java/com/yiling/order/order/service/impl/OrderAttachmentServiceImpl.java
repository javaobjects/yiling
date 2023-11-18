package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderAttachmentMapper;
import com.yiling.order.order.entity.OrderAttachmentDO;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.service.OrderAttachmentService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 订单相关附件 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-12
 */
@Service
public class OrderAttachmentServiceImpl extends BaseServiceImpl<OrderAttachmentMapper, OrderAttachmentDO> implements OrderAttachmentService {

    @Override
    public boolean saveBatch(Long orderId, String orderNo, OrderAttachmentTypeEnum attachmentTypeEnum, List<String> attachmentKeyList, Long opUserId) {
        if (CollUtil.isEmpty(attachmentKeyList)) {
            return true;
        }

        List<OrderAttachmentDO> list = CollUtil.newArrayList();
        for (String fileKey : attachmentKeyList) {
            OrderAttachmentDO entity = new OrderAttachmentDO();
            entity.setOrderId(orderId);
            entity.setOrderNo(orderNo);
            entity.setFileType(attachmentTypeEnum.getCode());
            entity.setFileKey(fileKey);
            entity.setOpUserId(opUserId);
            list.add(entity);
        }
        return this.baseMapper.batchInsert(list) > 0;
    }

    @Override
    public List<OrderAttachmentDO> listByOrderId(Long orderId, OrderAttachmentTypeEnum attachmentTypeEnum) {
        QueryWrapper<OrderAttachmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderAttachmentDO::getOrderId, orderId)
                .eq(OrderAttachmentDO::getFileType, attachmentTypeEnum.getCode())
                .orderByAsc(OrderAttachmentDO::getId);
        List<OrderAttachmentDO> list = this.list(queryWrapper);
        return CollUtil.isEmpty(list) ? ListUtil.empty() : list;
    }

    /**
     * @param orderId            订单id
     * @param attachmentTypeEnum 类型
     * @return
     */
    @Override
    public Boolean deleteByOrderId(Long orderId, OrderAttachmentTypeEnum attachmentTypeEnum,Long opUserId) {
        QueryWrapper<OrderAttachmentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderAttachmentDO :: getOrderId,orderId)
                .eq(OrderAttachmentDO :: getFileType,attachmentTypeEnum.getCode());

        OrderAttachmentDO attachment = new OrderAttachmentDO();
        attachment.setOpTime(new Date());
        attachment.setOpUserId(opUserId);
        batchDeleteWithFill(attachment,wrapper);
        return true;
    }
}
