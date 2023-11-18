package com.yiling.user.system.dto.request;

import java.util.List;

import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建外部员工账户审核信息 Request
 *
 * @author: xuan.zhou
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateStaffExternaAuditRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 销售区域是否全国：0-否 1-是
     */
    private Integer salesAreaAllFlag;

    /**
     * 销售区域
     */
    private List<LocationTreeDTO> salesAreaTree;

    /**
     * 身份证正面照KEY
     */
    private String idCardFrontPhotoKey;

    /**
     * 身份证反面照KEY
     */
    private String idCardBackPhotoKey;
}
