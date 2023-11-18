package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 向分组中添加查询结果的客户 Form
 *
 * @author: lun.yu
 * @date: 2022-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddResultGroupCustomersRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分组ID
     */
    @NotNull
    private Long groupId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 企业类型
     */
    private Integer type;

}
