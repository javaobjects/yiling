package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 视频拒接及未接通记录
 *
 * @author: fan.shen
 * @date: 2023/4/7
 */
@Data
@Accessors(chain = true)
public class VideoRefuseOrNotThroughRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 状态 1：未拨通 2：用户拒接
     */
    private Integer status;

}