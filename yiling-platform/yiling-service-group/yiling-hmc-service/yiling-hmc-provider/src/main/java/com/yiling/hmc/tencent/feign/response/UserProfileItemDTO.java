package com.yiling.hmc.tencent.feign.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UserProfileItemDTO
 */
@NoArgsConstructor
@Data
public class UserProfileItemDTO {
    /**
     * toAccount
     */
    private String toAccount;
    /**
     * profileItem
     */
    private List<ProfileItemDTO> profileItem;
    /**
     * resultCode
     */
    private Integer resultCode;
    /**
     * resultInfo
     */
    private String resultInfo;
}
