package com.yiling.hmc.insurance.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 保存理赔资料
 * @author: fan.shen
 *
 * @date: 2022/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveClaimInformationRequest extends BaseRequest {

    /**
     * 保险服务商id
     */
    private Long id;


    /**
     * 订单小票
     */
    private List<String> orderReceiptsList;

    /**
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证背面
     */
    private String idCardBack;

    /**
     * 手写签名
     */
    private String handSignature;
}
