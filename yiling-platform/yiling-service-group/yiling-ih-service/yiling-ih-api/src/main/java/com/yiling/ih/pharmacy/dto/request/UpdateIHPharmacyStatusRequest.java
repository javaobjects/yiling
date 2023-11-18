package com.yiling.ih.pharmacy.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 修改终端药店合作状态 Request
 *
 * @author: fan.shen
 * @date: 2023/5/22
 */
@Data
@Accessors(chain = true)
public class UpdateIHPharmacyStatusRequest implements java.io.Serializable {

    /**
     * 合作状态 0：开启 1：关闭
     */
    private Integer cooperationState;

    private Integer id;

}
