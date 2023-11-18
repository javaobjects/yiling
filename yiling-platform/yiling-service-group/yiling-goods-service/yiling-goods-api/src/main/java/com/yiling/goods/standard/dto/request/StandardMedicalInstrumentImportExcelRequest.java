package com.yiling.goods.standard.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 StandardMedicalInstrumentImportExcelRequest
 * @描述
 * @创建时间 2022/8/10
 * @修改人 shichen
 * @修改时间 2022/8/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardMedicalInstrumentImportExcelRequest extends BaseRequest {

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer isCn;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7医疗器械
     */
    private Integer goodsType;

    /**
     * 商品名称
     */
    private String  name;

    /**
     * 商品所属经营范围
     */
    private Integer businessScope;

    /**
     * 备案凭证编号/注册证编号
     */
    private String  licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 一级分类
     */
    private String standardCategoryName1;

    /**
     * 二级分类
     */
    private String standardCategoryName2;

    /**
     * 通用名
     */
    private String commonName;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 结构组成
     */
    private String structure;

    /**
     *
     */
    private String noteEvents;

    /**
     * 适用范围
     */
    private String useScope;

    /**
     * 储藏条件
     */
    private String storageConditions;

    /**
     * 使用说明
     */
    private String usageDosage;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 有效期
     */
    private String expirationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 规格
     */
    private String sellSpecifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * YPID编码
     */
    private String ypidCode;
}
