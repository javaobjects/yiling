package com.yiling.user.system.dto.request;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加注销账号 Request
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDeregisterAccountRequest extends BaseRequest {

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
     * 来源：1-销售助手APP 2-大运河APP 3-医药代表APP
	 */
	private Integer source;

    /**
     * 终端类型：1-Android 2-IOS
     */
	private Integer terminalType;

    /**
     * 注销原因
     */
    private String applyReason;

}
