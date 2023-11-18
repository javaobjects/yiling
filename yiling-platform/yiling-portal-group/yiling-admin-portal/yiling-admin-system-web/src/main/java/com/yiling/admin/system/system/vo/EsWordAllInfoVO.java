package com.yiling.admin.system.system.vo;

import com.yiling.search.word.bo.EsWordExpansionBO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 EsWordAllInfoVO
 * @描述
 * @创建时间 2022/4/27
 * @修改人 shichen
 * @修改时间 2022/4/27
 **/
@Data
@ApiModel
public class EsWordAllInfoVO {

    /**
     * 词语主体
     */
    @ApiModelProperty("词语主体")
    private String mainWord;

    /**
     * 扩展词
     */
    @ApiModelProperty("扩展词")
    private EsWordInfoVO expansionWord;

    /**
     * 停止词
     */
    @ApiModelProperty("停止词")
    private EsWordInfoVO stopWord;

    /**
     * 单向同义词
     */
    @ApiModelProperty("单向同义词")
    private EsWordInfoVO oneWaySynonym;

    /**
     * 双向同义词
     */
    @ApiModelProperty("双向同义词")
    private EsWordInfoVO twoWaySynonym;
}
