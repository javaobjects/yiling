package com.yiling.user.enterprise.bo;

import lombok.Data;

/**
 *  统计可采购销售渠道商的个数 BO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8
 */
@Data
public class CountSellerChannelBO {
    /**
     * 渠道商个数
     */
    private Integer channelNum;

    /**
     * 渠道商ID
     */
    private Long channelId;
}
