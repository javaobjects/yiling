package com.yiling.cms.content.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IHPatientContentPageDTO extends BaseDTO {

    private static final long serialVersionUID = -8755429127432498324L;


    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;


    /**
     * 状态 1未发布 2已发布
     */
    private Integer status;


    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 医生id
     */
    private Long doctorId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
