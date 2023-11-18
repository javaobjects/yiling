package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 秒杀特价
 * @author: fan.shen
 * @date: 2022/1/14
 */
@Data
@Accessors(chain = true)
public class PromotionSecKillSpecialRequest extends BaseRequest implements Serializable {

    /**
     * 终端身份 1-会员，2-非会员
     */
    private Integer terminalType;

    /**
     * 允许购买区域 1-全部，2-部分
     */
    private Integer permittedAreaType;

    /**
     * 允许购买区域明细json
     */
    private String  permittedAreaDetail;

    /**
     * 企业类型 1-全部，2-部分
     */
    private Integer permittedEnterpriseType;

    /**
     * 企业类型json
     */
    private String  permittedEnterpriseDetail;

}
