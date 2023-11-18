package com.yiling.basic.article.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ArticleDTO extends BaseDTO {

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章描述
     */
    private String articleDesc;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章状态 1-启用，2-停用
     */
    private Integer articleStatus;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
