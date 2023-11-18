package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 采购申请审核 Request
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Data
public class UpdatePurchaseApplyStatusRequest extends BaseRequest {

   /**
    * ID
    */
   private Long id;

   /**
    * 审核状态：2-审核通过 3-审核驳回
    */
   private Integer authStatus;

   /**
    * 驳回原因
    */
   private String authRejectReason;
}
