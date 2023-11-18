package com.yiling.ih.pharmacy.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 同步终端药店 Request
 *
 * @author: fan.shen
 * @date: 2023/5/22
 */
@Data
@Accessors(chain = true)
public class SyncPharmacyRequest implements java.io.Serializable {

    /**
     * eid
     */
    private Long hmcEid;

    /**
     * 	ename
     */
    private String pharmacyName;

    /**
     * 联系人
     */
    private String contactPersonnel;

    /**
     * 联系人电话
     */
    private String mobile;

    /**
     * 详细地址
     */
    private String address;


}
