package com.yiling.admin.erp.statistics.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlushGoodsSpecIdForm extends BaseForm {

    @ApiModelProperty("企业id")
    private Long eid;

    private List<FlushDataForm> flushDataList;

    @Data
    public static class FlushDataForm {

        @ApiModelProperty("商品名称")
        private String goodsName;

        @ApiModelProperty("商品规格")
        private String spec;

        @ApiModelProperty("厂家")
        private String manufacturer;

        @ApiModelProperty("推荐商品名称")
        private String recommendGoodsName;

        @ApiModelProperty("推荐商品规格")
        private String recommendSpec;

        @ApiModelProperty("推荐商品名称规格id")
        private String recommendSpecificationId;

        @ApiModelProperty("推荐分数")
        private Integer recommendScore;
    }
}
