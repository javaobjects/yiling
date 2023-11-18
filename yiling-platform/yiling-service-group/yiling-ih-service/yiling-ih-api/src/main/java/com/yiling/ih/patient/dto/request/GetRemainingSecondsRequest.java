package com.yiling.ih.patient.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/6/7 0007
 */
@Data
@Accessors(chain = true)
public class GetRemainingSecondsRequest implements Serializable {

    /**
     * 房间号
     */
    private String roomId;
}
