package com.yiling.ih.patient.dto;

import lombok.Data;

import java.util.List;

/**
 * 处方详情对象
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
public class HmcPrescriptionDetailDTO implements java.io.Serializable {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * 处方id
     */
    private Integer id;

    /**
     * 处方状态（0:医生待签名 1:待购买 2:已作废 3:已过有效期 4:已支付(待审核)  5:审方通过 6:审方未通过 ）
     */
    private Integer status;

    /**
     * 处方url
     */
    private String url;

    /**
     * 医生id
     */
    private Integer docId;

}
