package com.yiling.user.member.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新推广方发送通知 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdatePromoterSendRequest implements Serializable {


    private static final long serialVersionUID = 7845235094969161173L;

    /**
     * 会员订单号
     */
    private String orderNo;

    /**
     * 推广方id
     */
    private Long promoterId;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer source;

}
