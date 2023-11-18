package com.yiling.cms.content.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 草稿
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DraftDTO extends BaseDTO {


    private static final long serialVersionUID = -3247564277663035333L;
    private String category;

    private Long categoryId;

    /**
     * 标题
     */
    private String title;


    /**
     * 修改时间
     */
    private Date updateTime;

}
