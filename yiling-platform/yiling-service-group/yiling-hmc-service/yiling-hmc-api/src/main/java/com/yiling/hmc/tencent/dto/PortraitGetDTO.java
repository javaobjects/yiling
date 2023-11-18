package com.yiling.hmc.tencent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PortraitGetDTO implements Serializable {

    private static final long serialVersionUID = 7944789822838179571L;

    /**
     * userProfileItem
     */
    private List<UserProfileItemDTO> userProfileItem;
    /**
     * actionStatus
     */
    private String actionStatus;
    /**
     * errorCode
     */
    private Integer errorCode;
    /**
     * errorInfo
     */
    private String errorInfo;
    /**
     * errorDisplay
     */
    private String errorDisplay;
}
