package com.yiling.hmc.tencent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * UserProfileItemDTO
 */
@NoArgsConstructor
@Data
public class UserProfileItemDTO implements Serializable {
    /**
     * toAccount
     */
    private String To_Account;
    /**
     * profileItem
     */
    private List<ProfileItemDTO> ProfileItem;
    /**
     * resultCode
     */
    private Integer ResultCode;
    /**
     * resultInfo
     */
    private String ResultInfo;
}
