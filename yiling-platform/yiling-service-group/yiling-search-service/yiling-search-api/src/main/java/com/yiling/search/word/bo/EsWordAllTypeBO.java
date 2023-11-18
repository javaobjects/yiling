package com.yiling.search.word.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsWordAllTypeBO
 * @描述
 * @创建时间 2022/4/26
 * @修改人 shichen
 * @修改时间 2022/4/26
 **/
@Data
public class EsWordAllTypeBO implements Serializable {

    /**
     * 词语主体
     */
    private String mainWord;

    /**
     * 扩展词
     */
    private EsWordExpansionBO expansionWord;

    /**
     * 停止词
     */
    private EsWordExpansionBO stopWord;

    /**
     * 单向同义词
     */
    private EsWordExpansionBO oneWaySynonym;

    /**
     * 双向同义词
     */
    private EsWordExpansionBO twoWaySynonym;

}
