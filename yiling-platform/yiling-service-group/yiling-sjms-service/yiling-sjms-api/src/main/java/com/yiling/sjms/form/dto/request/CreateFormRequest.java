package com.yiling.sjms.form.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 创建基础表单信息 Request
 *
 * @author xuan.zhou
 * @date 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFormRequest extends BaseRequest {

    /**
     * 单据编号
     */
    private String code;

    /**
     * 单据名称
     */
    private String name;

    /**
     * 单据类型，参见FormTypeEnum枚举
     */
    private Integer type;

    /**
     * 备注
     */
    @Length(max = 500)
    private String remark;
    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    private Integer transferType;

}
