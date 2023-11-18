package com.yiling.sjms.wash.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
public class UnlockCustomerClassDetailPageVO extends BaseVO {

    @ApiModelProperty(value = "合计")
    private Integer total;

    @ApiModelProperty(value = "已分类数量")
    private Integer classifiedCount;

    @ApiModelProperty(value = "未分类数量")
    private Integer unclassifiedCount;

    @ApiModelProperty(value = "列表数据")
    private Page<UnlockCustomerClassDetailVO> page;

    public UnlockCustomerClassDetailPageVO() {
        this.total = 0;
        this.classifiedCount = 0;
        this.unclassifiedCount = 0;
    }
}
