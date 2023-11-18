package com.yiling.ih.patient.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 获取IH商品信息 request
 *
 * @author fan.shen
 * @date 2023-05-18
 */
@Data
public class HmcGetIHGoodsInfoRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * IH合作商药品id集合
     */
    private List<Long> ids;
}
