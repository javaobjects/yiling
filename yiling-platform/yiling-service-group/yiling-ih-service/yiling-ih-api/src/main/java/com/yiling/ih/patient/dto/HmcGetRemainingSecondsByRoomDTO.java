package com.yiling.ih.patient.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/6/7 0007
 */
@Data
public class HmcGetRemainingSecondsByRoomDTO implements Serializable {

    /**
     * 剩余秒数
     */
    private Integer seconds;
}
