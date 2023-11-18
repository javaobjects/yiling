package com.yiling.sales.assistant.message.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.message.dto.MessageDTO;
import com.yiling.sales.assistant.message.dto.request.CustomerMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.MessageQueryPageRequest;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageBuildRequest;
import com.yiling.sales.assistant.message.entity.MessageDO;
import com.yiling.sales.assistant.message.enums.MessageTypeEnum;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-17
 */
public interface MessageService extends BaseService<MessageDO> {

    /**
     * 创建订单维度消息
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean buildOrderMessage(OrderMessageBuildRequest request);

    /**
     * 创建订单维度消息
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean buildOrderListMessage(OrderListMessageBuildRequest request);

    /**
     * 创建客户维度消息
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean buildCustomerMessage(CustomerMessageBuildRequest request);

    /**
     * app消息模块分页查询api
     *
     * @param request   请求参数
     * @return  消息分页集合
     */
    Page<MessageDO> queryPage(MessageQueryPageRequest request);

    /**
     * 新增消息
     *
     * @param saMessageDTO  请求参数
     * @return  成功/失败
     */
    boolean add(MessageDTO saMessageDTO);

    /**
     * 批次新增消息
     *
     * @param messageDTOList    请求参数
     * @return  成功/失败
     */
    boolean addBatch(List<MessageDTO> messageDTOList);

    /**
     * 修改消息
     *
     * @param saMessageDTO  请求参数
     * @return  成功/失败
     */
    boolean edit(MessageDTO saMessageDTO);

    /**
     * 根据消息id查询消息
     *
     * @param id    消息id
     * @return  消息数据信息
     */
    MessageDO queryById(Long id);

    /**
     * 查询某人某些节点未读消息的数量
     *
     * @param nodeList  节点
     * @param userId    用户id
     * @return  未读数量
     */
    Integer countNotReadByNodeList(List<Integer> nodeList, Long userId);

    /**
     * 根据类型查询未读消息数量
     *
     * @param messageTypeEnum   消息类型
     * @param userId    用户id
     * @return  数量
     */
    Integer countNotReadByType(MessageTypeEnum messageTypeEnum, Long userId);
}
