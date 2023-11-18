package com.yiling.hmc.remind.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用药提醒结果累
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedsRemindResult {


    /**
     * 用药提醒id
     */
    private Long medsRemindId;

    /**
     * 提醒时间
     */
    private Date remindTime;
}
