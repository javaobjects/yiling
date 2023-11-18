package com.yiling.ih.user.dto;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 保存患者信息 Request
 *
 * @author: xuan.zhou
 * @date: 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientTagRequest extends BaseRequest {

    /**
     * 标签 ，号分隔
     */
    private String tag;

    /**
     * 标签类型 选药品时传0 疾病传1 门诊/疾病 2
     */
    private Integer tagType;

}
