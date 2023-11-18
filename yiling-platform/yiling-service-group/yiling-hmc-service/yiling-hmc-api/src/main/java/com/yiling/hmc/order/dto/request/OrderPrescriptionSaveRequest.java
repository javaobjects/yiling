package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存处方信息请求参数
 *
 * @author: yong.zhang
 * @date: 2022/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionSaveRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 开方医生
     */
    private String doctor;

    /**
     * 诊断结果
     */
    private String interrogationResult;

    /**
     * 处方图片
     */
    private String prescriptionSnapshotUrl;
}
