package com.yiling.cms.question.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医药代表回复信息关联表
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionReplyResourceDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private Long replyId;

    /**
     * 关联类型 1-关联文献 2-关联链接 3-图片说明 4-关联附件 
     */
    private Integer type;

    /**
     * 文献ID
     */
    private Long documentId;

    /**
     * 链接
     */
    private String url;

    /**
     * 文件图片key
     */
    private String resourceKey;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
