package com.yiling.sjms.manor.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/5/15
 */
@Data
@Accessors(chain = true)
public class ManorRelationDTO implements Serializable {

    private static final long serialVersionUID = -7149819213282276143L;
    /**
     * 子表单id
     */
    private Long id;
    /**
     * 品类名称
     */
    private String categoryName;




    private String manorNo;
    /**
     * 辖区名称
     */
    private String manorName;


    /**
     * 新辖区名称
     */
    private String newManorName;
    /**
     * 新辖区编码
     */
    private String newManorNo;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 旧辖区id
     */
    private Long manorId;

    /**
     * 新辖区id
     */
    private Long newManorId;

    /**
     * 是否选中
     */
    private Boolean isChecked;
}