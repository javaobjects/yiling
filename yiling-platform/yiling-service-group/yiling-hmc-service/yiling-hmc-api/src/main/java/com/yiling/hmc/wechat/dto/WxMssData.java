package com.yiling.hmc.wechat.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/26
 */
@Data
@Builder
public class WxMssData {

    private String value;

    private String color;
}
