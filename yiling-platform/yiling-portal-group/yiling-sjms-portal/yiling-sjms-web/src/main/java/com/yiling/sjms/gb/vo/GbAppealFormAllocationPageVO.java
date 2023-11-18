package com.yiling.sjms.gb.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFormAllocationPageVO<T> extends Page<T> {

    /**
     * 与团购数量不一致标识：false-否（即一致） true-是（即不一致）
     */
    @ApiModelProperty(value = "与团购数量不一致标识：false-否（即一致） true-是（即不一致）")
    private Boolean errorFlag;

    /**
     * 与团购数量不一致提示信息：errorFlag=true时有此信息
     */
    @ApiModelProperty(value = "与团购数量不一致提示信息：errorFlag=true时有此信息")
    private String errorMsg;

    public GbAppealFormAllocationPageVO(){
        this.errorMsg = "";
    }

}
