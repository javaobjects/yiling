package com.yiling.data.center.admin.goods.form;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsForm extends BaseForm {

    @ApiModelProperty(value = "商品库ID", example = "1111")
    private Long sellSpecificationsId;

    @ApiModelProperty(value = "商品库ID", example = "1111")
    private Long  standardId;

    @ApiModelProperty(value = "商品类型", example = "1111")
    private Integer goodsType;

    @ApiModelProperty(value = "是否国产", example = "1111")
    private Integer isCn;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号", example = "Z109090")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家", example = "以岭")
    private String manufacturer;

    /**
     * 生产厂家地址
     */
    @ApiModelProperty(value = "生产厂家地址", example = "石家庄")
    private String manufacturerAddress;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    @Length(max = 100)
    private String name;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "自己公司销售规格", example = "1片")
    private String specifications;

    /**
     * 规格单位
     */
    @ApiModelProperty(value = "规格单位", example = "盒")
    private String unit;

    /**
     * 开通产品线
     */
    @ApiModelProperty(value = "开通产品线")
    private GoodsLineForm goodsLine;

    /**
     * 中药饮片说明书信息
     */
    @ApiModelProperty(value = "中药饮片说明书信息")
    private InstructionsDecoctionForm decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    @ApiModelProperty(value = "消杀品说明书信息")
    private InstructionsDisinfectionForm disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    @ApiModelProperty(value = "食品说明书信息")
    private InstructionsFoodsForm foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    @ApiModelProperty(value = "保健食品说明书信息")
    private InstructionsHealthForm healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    @ApiModelProperty(value = "中药材说明书信息")
    private InstructionsMaterialsForm materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    @ApiModelProperty(value = "药品说明书信息")
    private InstructionsGoodsForm goodsInstructionsInfo;

    /**
     * 医疗器械说明书信息
     */
    @ApiModelProperty(value = "医疗器械说明书信息")
    private InstructionsMedicalInstrumentForm medicalInstrumentInfo;

    /**
     * 配方颗粒说明书信息
     */
    @ApiModelProperty(value = "配方颗粒说明书信息")
    private InstructionsDispensingGranuleForm dispensingGranuleInfo;

    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private List<GoodsPicForm> picBasicsInfoList;

}
