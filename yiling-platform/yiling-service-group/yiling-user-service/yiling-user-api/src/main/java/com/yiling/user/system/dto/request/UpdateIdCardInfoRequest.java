package com.yiling.user.system.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新身份证号及身份证附件 Request
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIdCardInfoRequest extends BaseRequest {

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 姓名
     */
    @NotEmpty
    private String name;

    /**
     * 身份证号
     */
    @NotEmpty
    private String idNumber;

    /**
     * 身份证正面照KEY
     */
    @NotEmpty
    private String idCardFrontPhotoKey;

    /**
     * 身份证反面照KEY
     */
    @NotEmpty
    private String idCardBackPhotoKey;
}
