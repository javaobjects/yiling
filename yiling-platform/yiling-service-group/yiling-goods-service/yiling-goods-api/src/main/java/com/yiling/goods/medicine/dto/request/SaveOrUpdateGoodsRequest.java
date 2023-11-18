package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long repGoodsId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    private Integer auditStatus;

    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    private Integer goodsStatus;

	/**
	 * 下架原因：1平台下架 2质管下架 3供应商下架
	 */
	private Integer outReason;

    /**
     * 药品类别：1药品 2非药品
     */
    private Integer materialType;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 商品内码
     */
    private String sn;

    /**
     * 商品编码
     */
    private String inSn;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 商品工业品还是商业品
     */
    private Integer enterpriseType;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 通用名称
     */
    private String commonName;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产厂家
     */
    private String manufacturerCode;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 规格单位
     */
    private String unit;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    /**
     * 销售规格
     */
    private String sellUnit;

    /**
     * 商品基价
     */
    private BigDecimal price;

    /**
     * 商品库存信息
     */
    private Long qty;

    /**
     * 中包装
     */
    private Long middlePackage;

    /**
     * 大包装
     */
    private Long bigPackage;

    /**
     * 是否拆包销售：1可拆0不可拆
     */
    private Integer canSplit;

    /**
     * 商品分类id1
     */
    private Long categoryId1;

    /**
     * 商品分类id2
     */
    private Long categoryId2;

    /**
     * 商品别名
     */
    private String aliasName;

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer isCn;

    /**
     * 专利标识: 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 备注
     */
    private String remark;

    /**
     * 剂型名称
     */
    private String gdfName;

    /**
     * 剂型规格
     */
    private String gdfSpecifications;

    /**
     * 处方类型：1处方药 2甲类非处方药 3乙类非处方药 4其他
     */
    private Integer otcType;

    /**
     * 是否医保：1是 2非 3未采集到相关信息
     */
    private Integer isYb;

    /**
     * 管制类型：0非管制1管制
     */
    private Integer controlType;

    /**
     * 药品本位码
     */
    private String goodsCode;

    /**
     * 复方制剂具体成分
     */
    private String ingredient;

    /**
     * 特殊成分： 0不含麻黄碱 1含麻黄碱
     */
    private Integer specialComposition;

    /**
     * 质量标准类别：1中国药典2地方标准3其他
     */
    private Integer qualityType;

    /**
     * 来源
     */
    private String goodsSource;

    /**
     * 毛重
     */
    private String roughWeight;

    /**
     * 等级
     */
    private String goodsGrade;

    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 产品类型
     */
    private String productClassification;

    /**
     * 产品标准号
     */
    private String productStandardCode;


    /**
     * 生产日期
     */
    private Date manufacturingDate;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 数据来源：1-平台录入2-平台导入3-erp对接
     */
    private Integer source;

    /**
     * 开通产品线对象
     */
    private SaveGoodsLineRequest goodsLineInfo;

    /**
     * 中药饮片说明书信息
     */
    private InstructionsDecoctionRequest decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    private InstructionsDisinfectionRequest disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    private InstructionsFoodsRequest foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    private InstructionsHealthRequest healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    private InstructionsMaterialsRequest materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    private InstructionsGoodsRequest goodsInstructionsInfo;

    /**
     * 医疗器械说明书
     */
    private InstructionsMedicalInstrumentRequest medicalInstrumentInfo;

    /**
     * 配方颗粒说明书
     */
    private InstructionsDispensingGranuleRequest dispensingGranuleInfo;

    /**
     * 图片信息
     */
    private List<SaveGoodsPicRequest> picBasicsInfoList;

    /**
     * 商品sku信息
     */
    private List<SaveOrUpdateGoodsSkuRequest> goodsSkuList;

    /**
     * pop的企业编号
     */
    private List<Long> popEidList;

    public BigDecimal getPrice() {
        if(price!=null) {
            return price.setScale(4,BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }
}
