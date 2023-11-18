package com.yiling.marketing.presale.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddPresaleActivityRequest extends BaseRequest {

    /**
     * 活动id-修改时需要
     */
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动类型（1-策略满赠,2-支付促销）
     */
    private Integer type;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    private Date beginTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 运营备注
     */
    private String operatingRemark;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    private Integer bear;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 选择端口（1-B端；2-c端）逗号隔开
     */
    private String portSelected;
}
