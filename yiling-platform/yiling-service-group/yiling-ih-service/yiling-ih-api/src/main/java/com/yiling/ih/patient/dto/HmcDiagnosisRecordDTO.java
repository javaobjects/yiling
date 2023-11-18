package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 问诊订单DTO
 *
 * @author: fan.shen
 * @data: 2023/05/18
 */
@Data
@Accessors(chain = true)
public class HmcDiagnosisRecordDTO {

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 问诊单价格
     */
    private BigDecimal diagnosisRecordPrice;

    /**
     * 问诊单状态 0创建中(无) 1待支付 2已取消-超时作废 3已支付(无) 4待问诊 5问诊中 6退款中 7退款成功 8退款失败 9免费无需退款 10过期已退中（医生未回复-包括图文音视频） 11问诊结束 12已回绝（医生回绝音视频） 13过期已退失败（医生未回复-包括图文音视频） 14过期已退成功（医生未回复-包括图文音视频） 15 停诊退号失败  16 停诊退号成功
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付状态 0待支付,1支付中,2支付失败,3已支付,4免费,5超时取消,6退款中,7退款成功,8退款失败,9订单完成
     */
    private Integer payStatus;

}
