package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 获取IM会话
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class GetIMConversationListRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 用户id
     */
    private Long fromUserId;

}
