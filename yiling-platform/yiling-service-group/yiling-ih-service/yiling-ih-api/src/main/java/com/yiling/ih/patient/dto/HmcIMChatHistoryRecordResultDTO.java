package com.yiling.ih.patient.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 获取IM聊天历史记录
 *
 * @author: fan.shen
 * @data: 2023/01/31
 */
@Data
public class HmcIMChatHistoryRecordResultDTO implements java.io.Serializable{

    private int total;

    private List<HmcIMChatHistoryRecordDTO> list;


}
