package com.yiling.sales.assistant.message.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.MessageDTO;
import com.yiling.sales.assistant.message.dto.NotReadMessageTypeDTO;
import com.yiling.sales.assistant.message.dto.request.CustomerMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.MessageQueryPageRequest;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageBuildRequest;
import com.yiling.sales.assistant.message.entity.MessageDO;
import com.yiling.sales.assistant.message.enums.MessageIsReadEnum;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.MessageTypeEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.sales.assistant.message.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageApiImpl implements MessageApi {

    private final MessageService saMessageService;

    @Override
    public boolean buildOrderMessage(OrderMessageBuildRequest request) {
        log.info("message buildOrderMessage start, request:[{}]", request);
        return saMessageService.buildOrderMessage(request);
    }

    @Override
    public boolean buildOrderListMessage(OrderListMessageBuildRequest request) {
        log.info("message buildOrderListMessage start, request:[{}]", request);
        return saMessageService.buildOrderListMessage(request);
    }

    @Override
    public boolean buildCustomerMessage(CustomerMessageBuildRequest request) {
        log.info("message buildCustomerMessage start, request:[{}]", request);
        return saMessageService.buildCustomerMessage(request);
    }

    @Override
    public Page<MessageDTO> queryPage(MessageQueryPageRequest request) {
        // 业务类型，0 所有，1-业务进度，2-发布任务，3-促销政策，4-学术下方
        Page<MessageDO> messagePage = saMessageService.queryPage(request);
        return PojoUtils.map(messagePage, MessageDTO.class);
    }

    // =========================================上面的是消息模块1.0版本==================================================

    @Override
    public boolean create(MessageDTO saMessageDTO) {
        return saMessageService.add(saMessageDTO);
    }

    @Override
    public boolean build(Long userId, Long eid, SourceEnum sourceEnum, MessageRoleEnum messageRoleEnum, MessageNodeEnum messageNodeEnum,
                         String code) {
        MessageDTO saMessageDTO = new MessageDTO();
        saMessageDTO.setUserId(userId).setEid(eid).setSource(sourceEnum.getCode()).setRole(messageRoleEnum.getCode())
            .setType(MessageTypeEnum.PROGRESS.getCode()).setNode(messageNodeEnum.getCode()).setIsRead(MessageIsReadEnum.NOT_READ.getCode())
            .setCode(code).setContent(messageNodeEnum.getName()).setCreateTime(new Date());
        return saMessageService.add(saMessageDTO);
    }

    @Override
    public boolean buildBatch(Long userId, Long eid, SourceEnum sourceEnum, MessageRoleEnum messageRoleEnum, MessageNodeEnum messageNodeEnum,
                              List<String> codeList) {
        List<MessageDTO> messageDTOList = new ArrayList<>();
        for (String code : codeList) {
            MessageDTO saMessageDTO = new MessageDTO();
            saMessageDTO.setUserId(userId).setEid(eid).setSource(sourceEnum.getCode()).setRole(messageRoleEnum.getCode())
                .setType(MessageTypeEnum.PROGRESS.getCode()).setNode(messageNodeEnum.getCode()).setIsRead(MessageIsReadEnum.NOT_READ.getCode())
                .setCode(code).setContent(messageNodeEnum.getName()).setCreateTime(new Date());
            messageDTOList.add(saMessageDTO);
        }
        return saMessageService.addBatch(messageDTOList);
    }

    @Override
    public boolean read(Long id) {
        MessageDO saMessageDO = saMessageService.queryById(id);
        if (MessageIsReadEnum.NOT_READ.getCode().equals(saMessageDO.getIsRead())) {
            MessageDTO saMessageDTO = new MessageDTO();
            saMessageDTO.setId(id);
            saMessageDTO.setIsRead(MessageIsReadEnum.READ.getCode());
            saMessageService.edit(saMessageDTO);
        }
        return true;
    }

    @Override
    public NotReadMessageTypeDTO countNotRead(Long userId) {
        NotReadMessageTypeDTO notReadMessageTypeDTO = new NotReadMessageTypeDTO();
        // 1 业务进度，2 发布任务
        {
            Integer businessProgressCount = saMessageService.countNotReadByType(MessageTypeEnum.PROGRESS, userId);
            notReadMessageTypeDTO.setBusinessProgressCount(businessProgressCount);
        }
        {
            Integer publishTaskCount = saMessageService.countNotReadByType(MessageTypeEnum.TASK, userId);
            notReadMessageTypeDTO.setPublishTaskCount(publishTaskCount);
        }
        return notReadMessageTypeDTO;
    }
}
