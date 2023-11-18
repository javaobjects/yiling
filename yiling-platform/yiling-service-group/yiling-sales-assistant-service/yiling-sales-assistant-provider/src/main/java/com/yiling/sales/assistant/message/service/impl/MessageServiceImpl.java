package com.yiling.sales.assistant.message.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.message.dao.MessageMapper;
import com.yiling.sales.assistant.message.dto.MessageDTO;
import com.yiling.sales.assistant.message.dto.request.CustomerMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.MessageQueryPageRequest;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageRequest;
import com.yiling.sales.assistant.message.entity.MessageDO;
import com.yiling.sales.assistant.message.enums.MessageTypeEnum;
import com.yiling.sales.assistant.message.service.MessageService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-17
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, MessageDO> implements MessageService {

    @Override
    public boolean buildOrderMessage(OrderMessageBuildRequest request) {
        OrderMessageRequest orderMessageRequest = request.getOrderMessageRequest();
        if (null == orderMessageRequest) {
            return false;
        }
        MessageDO messageDO = new MessageDO().setUserId(request.getUserId()).setEid(request.getEid()).setSource(request.getSourceEnum().getCode())
            .setRole(request.getMessageRoleEnum().getCode()).setType(MessageTypeEnum.PROGRESS.getCode())
            .setNode(request.getMessageNodeEnum().getCode()).setCode(request.getOrderMessageRequest().getCode())
            .setContent(request.getOrderMessageRequest().getOrderNo() + "," + request.getOrderMessageRequest().getCreateTime() + ","
                        + request.getMessageNodeEnum().getName())
            .setCreateTime(new Date());
        return this.save(messageDO);
    }

    @Override
    public boolean buildOrderListMessage(OrderListMessageBuildRequest request) {
        if (CollUtil.isEmpty(request.getOrderMessageRequestList())) {
            return false;
        }
        List<MessageDO> messageDTOList = new ArrayList<>();
        for (OrderMessageRequest orderMessageRequest : request.getOrderMessageRequestList()) {
            MessageDO messageDO = new MessageDO().setUserId(request.getUserId()).setEid(request.getEid()).setSource(request.getSourceEnum().getCode())
                .setRole(request.getMessageRoleEnum().getCode()).setType(MessageTypeEnum.PROGRESS.getCode())
                .setNode(request.getMessageNodeEnum().getCode()).setCode(orderMessageRequest.getCode())
                .setContent(
                    orderMessageRequest.getOrderNo() + "," + orderMessageRequest.getCreateTime() + "," + request.getMessageNodeEnum().getName())
                .setCreateTime(new Date());
            messageDTOList.add(messageDO);
        }
        return this.saveBatch(messageDTOList);
    }

    @Override
    public boolean buildCustomerMessage(CustomerMessageBuildRequest request) {
        if (null == request.getCode()) {
            return false;
        }
        MessageDO messageDO = new MessageDO().setUserId(request.getUserId()).setEid(request.getEid()).setSource(request.getSourceEnum().getCode())
            .setRole(request.getMessageRoleEnum().getCode()).setType(MessageTypeEnum.PROGRESS.getCode())
            .setNode(request.getMessageNodeEnum().getCode()).setCode(request.getCode())
            .setContent(request.getName() + "," + request.getCreateTime() + "," + request.getMessageNodeEnum().getName()).setCreateTime(new Date());
        return this.save(messageDO);
    }

    @Override
    public Page<MessageDO> queryPage(MessageQueryPageRequest request) {
        QueryWrapper<MessageDO> wrapper = new QueryWrapper<>();
        if (null != request.getType() && 0 != request.getType()) {
            wrapper.lambda().in(MessageDO::getType, request.getType());
        }
        if (null != request.getUserId()) {
            wrapper.lambda().eq(MessageDO::getUserId, request.getUserId());
        }
        wrapper.lambda().orderByDesc(MessageDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public boolean add(MessageDTO saMessageDTO) {
        MessageDO messageDO = PojoUtils.map(saMessageDTO, MessageDO.class);
        return this.save(messageDO);
    }

    @Override
    public boolean addBatch(List<MessageDTO> messageDTOList) {
        List<MessageDO> messageDOList = PojoUtils.map(messageDTOList, MessageDO.class);
        return this.saveBatch(messageDOList);
    }

    @Override
    public boolean edit(MessageDTO saMessageDTO) {
        MessageDO messageDO = PojoUtils.map(saMessageDTO, MessageDO.class);
        return this.updateById(messageDO);
    }

    @Override
    public MessageDO queryById(Long id) {
        return this.getById(id);
    }

    @Override
    public Integer countNotReadByNodeList(List<Integer> nodeList, Long userId) {
        QueryWrapper<MessageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MessageDO::getNode, nodeList).eq(MessageDO::getUserId, userId);
        return this.count(wrapper);
    }

    @Override
    public Integer countNotReadByType(MessageTypeEnum messageTypeEnum, Long userId) {
        QueryWrapper<MessageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MessageDO::getType, messageTypeEnum.getCode()).eq(MessageDO::getUserId, userId);
        return this.count(wrapper);
    }
}
