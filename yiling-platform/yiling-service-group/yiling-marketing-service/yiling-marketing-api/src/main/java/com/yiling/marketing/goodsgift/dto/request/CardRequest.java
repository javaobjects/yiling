package com.yiling.marketing.goodsgift.dto.request;

import lombok.Data;

/**
 * 虚拟物品卡号信息
 * @author:wei.wang
 * @date:2021/11/4
 */
@Data
public class CardRequest implements java.io.Serializable {
    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 密码
     */
    private String password;
}
