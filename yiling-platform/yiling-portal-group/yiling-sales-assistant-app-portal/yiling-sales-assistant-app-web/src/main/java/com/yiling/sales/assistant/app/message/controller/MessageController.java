package com.yiling.sales.assistant.app.message.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.message.form.MessageQueryPageForm;
import com.yiling.sales.assistant.app.message.vo.CustomerMessageVO;
import com.yiling.sales.assistant.app.message.vo.MessageVO;
import com.yiling.sales.assistant.app.message.vo.NotReadMessageTypeVO;
import com.yiling.sales.assistant.app.message.vo.OrderMessageVO;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.MessageDTO;
import com.yiling.sales.assistant.message.dto.NotReadMessageTypeDTO;
import com.yiling.sales.assistant.message.dto.request.MessageQueryPageRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * app消息模块查询
 *
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@RestController
@RequestMapping("/message")
@Api(tags = "销售助手APP的消息模块")
@Slf4j
public class MessageController extends BaseController {
    @DubboReference
    MessageApi saMessageApi;

    /**
     * 业务节点：
     * 订单维度：订单已下单未发货，未确认收货，已收货未开票，订单驳回
     * 客户维度：客户信息认证失败
     * <p>
     * 发布任务：跳转到任务页面
     */
    @ApiOperation(value = "用户app查询消息")
    @PostMapping("/queryPage")
    public Result<Page<MessageVO>> query(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid MessageQueryPageForm form) {
        // type:0 所有，1 业务进度，2 发布任务
        MessageQueryPageRequest request = PojoUtils.map(form, MessageQueryPageRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        Page<MessageDTO> messagePage = saMessageApi.queryPage(request);
        Page<MessageVO> voPage = PojoUtils.map(messagePage, MessageVO.class);
        for (MessageVO messageVO : voPage.getRecords()) {
            String content = messageVO.getContent();
            String[] strings = content.split(",");
            if (strings.length < 2) {
                return Result.success(voPage);
            }
            if (20 == messageVO.getNode()) {
                CustomerMessageVO customerMessageVO = new CustomerMessageVO().setName(strings[0]).setCreateTime(DateUtil.parse(strings[1]));
                messageVO.setCustomerMessageVO(customerMessageVO);
            } else {
                OrderMessageVO orderMessageVO = new OrderMessageVO().setOrderNo(messageVO.getCode()).setCreateTime(DateUtil.parse(strings[1]))
                    .setNode(messageVO.getNode());
                messageVO.setOrderMessageVO(orderMessageVO);
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "用户点击消息")
    @GetMapping("/read")
    public Result<Boolean> read(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        boolean read = saMessageApi.read(id);
        return Result.success(read);
    }

    @ApiOperation(value = "用户未读消息数量查看")
    @GetMapping("/notReadCount")
    public Result<NotReadMessageTypeVO> notReadCount(@CurrentUser CurrentStaffInfo staffInfo) {
        NotReadMessageTypeDTO notReadMessageTypeDTO = saMessageApi.countNotRead(staffInfo.getCurrentUserId());
        NotReadMessageTypeVO notReadMessageTypeVO = PojoUtils.map(notReadMessageTypeDTO, NotReadMessageTypeVO.class);
        return Result.success(notReadMessageTypeVO);
    }
}
