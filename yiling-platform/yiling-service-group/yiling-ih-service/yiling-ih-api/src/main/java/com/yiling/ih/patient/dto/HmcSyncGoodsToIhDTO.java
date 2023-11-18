package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;


/**
 * 我的-我的医生-咨询过列表
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcSyncGoodsToIhDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    /**
     * IH平台药品id
     */
    private Integer id;

    /**
     * IH平台药品名称
     */
    private String name;
}
