package com.yiling.ih.patient.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 获取IM聊天记录
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class GetIMChatHistoryRecordPageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 最后一条消息id
     */
    private Integer lastId;

    /**
     * 群组id
     */
    private String groupId;


}
