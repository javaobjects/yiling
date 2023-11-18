package com.yiling.hmc.tencent.feign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMUnReadMsgNumResponse implements java.io.Serializable {

    private static final long serialVersionUID = 7944789822838179571L;

    /**
     * actionStatus
     */
    private String ActionStatus;

    /**
     * errorInfo
     */
    private String ErrorInfo;

    /**
     * errorCode
     */
    private Integer ErrorCode;

    /**
     * allC2CUnreadMsgNum
     */
    private Integer AllC2CUnreadMsgNum;
}
