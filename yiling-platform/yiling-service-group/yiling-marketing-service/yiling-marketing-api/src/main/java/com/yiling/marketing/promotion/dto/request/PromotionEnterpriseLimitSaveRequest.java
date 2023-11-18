package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
public class PromotionEnterpriseLimitSaveRequest extends BaseRequest implements Serializable {

    /**
     * 企业ID
     */
    private Long   eid;

    /**
     * 企业名称
     */
    private String ename;
}
