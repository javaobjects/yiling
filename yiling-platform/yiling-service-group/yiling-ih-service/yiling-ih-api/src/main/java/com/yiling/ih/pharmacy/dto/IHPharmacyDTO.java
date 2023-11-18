package com.yiling.ih.pharmacy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 互联网医院药店
 */
@Data
@Accessors(chain = true)
public class IHPharmacyDTO implements java.io.Serializable {

    /**
     * 	IH合作药店id
     */
    private Long id;

    /**
     * 	IH合作商名称
     */
    private String pharmacyName;
}
