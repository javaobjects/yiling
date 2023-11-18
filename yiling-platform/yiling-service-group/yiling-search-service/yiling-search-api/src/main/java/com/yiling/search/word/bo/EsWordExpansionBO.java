package com.yiling.search.word.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.search.word.dto.EsWordExpansionDTO;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsWordExpansionBO
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Data
public class EsWordExpansionBO implements Serializable {
    private Long id;
    /**
     * 扩展词
     */
    private String word;
    /**
     * 扩展词类型 1：扩展词，2：停止词，3：单向同义词，4：双向同义词
     */
    private Integer type;

    /**
     * 状态 0：正常 1：停用
     */
    private Integer status;

    /**
     * 关联id
     */
    private Integer refId;

    /**
     * 关联词
     */
    private List<EsWordExpansionDTO> refWordList;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;
}
