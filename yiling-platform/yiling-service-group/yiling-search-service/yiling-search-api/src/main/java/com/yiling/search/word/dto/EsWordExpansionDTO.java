package com.yiling.search.word.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 EsWordExpansionDTO
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EsWordExpansionDTO extends BaseDTO {

    /**
     * 词语
     */
    private String word;
    /**
     * 扩展词类型 1：扩展词，2：停止词，3：单向同义词，4：双向同义词
     */
    private Integer type;
    /**
     * 关联词id
     */
    private Long refId;
    /**
     * 状态 0：正常 1：停用
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
