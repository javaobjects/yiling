package com.yiling.search.word.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.search.word.dto.EsWordExpansionDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateEsWordExpansionRequest
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateEsWordExpansionRequest extends BaseRequest {
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
     * 关联id（双向同义词refId为组id，单向同义词主词refId为0 关联词的refId为主词id）
     */
    private Long refId;

    /**
     * 关联词
     */
    private List<EsWordExpansionDTO> refWordList;
}
