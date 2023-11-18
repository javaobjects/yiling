package com.yiling.cms.content.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IHPatientHomeContentPageDTO extends BaseDTO {

    private static final long serialVersionUID = -8755429127432498324L;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;

    /**
     * 创建时间
     */
    private Date creatTime;
}
