package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送地址表
 * </p>
 *
 * @author gxl
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
public class InsertDeliveryAddressRequest extends BaseRequest {


    private static final long serialVersionUID = 3545431035155324800L;
    /**
     * 企业id
     */
    private Long eid;

    /**
     * 省id
     */
    private String provinceCode;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市id
     */
    private String cityCode;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区id
     */
    private String regionCode;

    /**
     * 区
     */
    private String regionName;

    /**
     * 地址
     */
    private String address;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;





}
