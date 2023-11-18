package com.yiling.sales.assistant.message.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.message.dto.MessageDTO;
import com.yiling.sales.assistant.message.dto.NotReadMessageTypeDTO;
import com.yiling.sales.assistant.message.dto.request.CustomerMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.MessageQueryPageRequest;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageBuildRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;

/**
 * 消息模块API
 * 
 * @author: yong.zhang
 * @date: 2021/9/17
 */
public interface MessageApi {

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
    Page<MessageDTO> queryPage(MessageQueryPageRequest request);

    // =========================================上面的是消息模块1.0版本==================================================

    /**
     * 创建消息
     *
     * @param saMessageDTO  消息创建请求实体
     * @return  成功/失败
     */
    boolean create(MessageDTO saMessageDTO);

    /**
     * 提供给各个模块创建消息的api
     * 消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败 6-发布任务
     *
     * @param userId          用户id
     * @param eid             企业id
     * @param sourceEnum      消息来源：1-POP 2-销售助手 3-B2B
     * @param messageRoleEnum 消息角色：1-对用户 2-对企业
     * @param messageNodeEnum 消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败
     * @param code            编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号
     * @return  成功/失败
     */
    boolean build(Long userId, Long eid, SourceEnum sourceEnum, MessageRoleEnum messageRoleEnum, MessageNodeEnum messageNodeEnum, String code);

    /**
     * 提供给各个模块创建消息的api
     * 消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败 6-发布任务
     *
     * @param userId          用户id
     * @param eid             企业id
     * @param sourceEnum      消息来源：1-POP 2-销售助手 3-B2B
     * @param messageRoleEnum 消息角色：1-对用户 2-对企业
     * @param messageNodeEnum 消息节点：1-订单已下单未发货 2-未确认收货 3-已收货未开票 4-订单驳回 5-客户信息认证失败
     * @param codeList        编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号
     * @return  成功/失败
     */
    boolean buildBatch(Long userId, Long eid, SourceEnum sourceEnum, MessageRoleEnum messageRoleEnum, MessageNodeEnum messageNodeEnum,
                       List<String> codeList);

    /**
     * 用户点击消息(阅读)
     *
     * @param id    消息id
     * @return  成功/失败
     */
    boolean read(Long id);

    /**
     * 统计各类型消息未数量
     *
     * @param userId    用户id
     * @return  消息数量统计结果
     */
    NotReadMessageTypeDTO countNotRead(Long userId);
}
