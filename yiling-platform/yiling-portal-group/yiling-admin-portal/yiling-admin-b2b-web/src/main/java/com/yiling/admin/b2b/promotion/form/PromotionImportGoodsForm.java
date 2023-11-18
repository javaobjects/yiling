package com.yiling.admin.b2b.promotion.form;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 满赠活动商品导入
 * </p>
 *
 * @author yong.zhang
 * @date 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionImportGoodsForm", description = "促销活动导入商品信息")
public class PromotionImportGoodsForm extends BaseForm {

    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "eidList")
    private String eidList;

    @ApiModelProperty(value = "活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "商家类型 1-以岭，2-非以岭")
    private Integer merchantType;

    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动；）")
    @NotNull(message = "活动分类不能为空")
    private Integer sponsorType;

    @ApiModelProperty(value = "活动类型（1-满赠,2-特价,3-秒杀）")
    @NotNull(message = "活动类型不能为空")
    private Integer type;
}
