package com.yiling.admin.data.center.standard.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsBasicInfoVO extends BaseVO {


    private static final long serialVersionUID = -121344608L;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    @ApiModelProperty(value = "商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品 7-医疗器械")
    private Integer goodsType;

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    @ApiModelProperty(value = "是否国产：1-国产 2-进口 3-出口")
    private Integer isCn;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;


    /**
     * 通用名称
     */
    @ApiModelProperty(value = "通用名称")
    private String commonName;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产地址")
    private String manufacturerAddress;

    /**
     * 分类id1
     */
    @ApiModelProperty(value = "分类id1")
    private String standardCategoryId1;

    /**
     * 分类id2
     */
    @ApiModelProperty(value = "分类id2")
    private String standardCategoryId2;

    /**
     * 分类名称1
     */
    @ApiModelProperty(value = "分类名称1")
    private String standardCategoryName1;

    /**
     * 分类名称2
     */
    @ApiModelProperty(value = "分类名称2")
    private String standardCategoryName2;

    /**
     * 商品别名
     */
    @ApiModelProperty(value = "商品别名")
    private String aliasName;

    /**
     * 剂型名称
     */
    @ApiModelProperty(value = "剂型名称")
    private String gdfName;

    /**
     * 剂型规格
     */
    @ApiModelProperty(value = "剂型规格")
    private String gdfSpecifications;

    /**
     * 处方类型：1处方药 2甲类非处方药 3乙类非处方药 4其他
     */
    @ApiModelProperty(value = "处方类型：1-处方药 2-甲类非处方药 3-乙类非处方药 4-其他")
    private Integer otcType;

    /**
     * 是否医保：1是 2非 3未采集到相关信息
     */
    @ApiModelProperty(value = "是否医保：1-是 2-非 3-未采集到相关信息")
    private Integer isYb;

    /**
     * 管制类型：0非管制 1管制
     */
    @ApiModelProperty(value = "管制类型：0-非管制 1-管制")
    private Integer controlType;

    /**
     * 药品本位码
     */
    @ApiModelProperty(value = "药品本位码")
    private String goodsCode;

    /**
     * 复方制剂具体成分
     */
    @ApiModelProperty(value = "复方制剂具体成分")
    private String ingredient;

    /**
     * 特殊成分：0不含麻黄碱 1含麻黄碱
     */
    @ApiModelProperty(value = "特殊成分：0-不含麻黄碱 1-含麻黄碱")
    private Integer specialComposition;

    /**
     * 质量标准类别：1中国药典 2地方标准 3其他
     */
    @ApiModelProperty(value = "处方类型：1-中国药典 2-地方标准 3-其他")
    private Integer qualityType;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String goodsSource;

    /**
     * 含量
     */
    @ApiModelProperty(value = "含量")
    private String roughWeight;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级")
    private String goodsGrade;

    /**
     * 执行标准
     */
    @ApiModelProperty(value = "执行标准")
    private String executiveStandard;

    /**
     * 批准日期
     */
    @ApiModelProperty(value = "批准日期")
    private String approvalDate;

    /**
     * 产品类型
     */
    @ApiModelProperty(value = "产品类型")
    private String productClassification;

    /**
     * 产品标准号/生产许可证编号
     */
    @ApiModelProperty(value = "产品标准号/生产许可证编号")
    private String productStandardCode;

    /**
     * 经营范围 1 ：一类医疗器械  2：二类医疗器械 3：三类医疗器械
     */
    @ApiModelProperty(value = "经营范围 1 ：一类医疗器械  2：二类医疗器械 3：三类医疗器械")
    private Integer businessScope;

    /**
     * 注册人
     */
    @ApiModelProperty(value = "注册人")
    private String registrant;

    /**
     * 注册人地址
     */
    @ApiModelProperty(value = "注册人地址")
    private String registrantAddress;

    /**
     * 代理人
     */
    @ApiModelProperty(value = "代理人")
    private String agent;

    /**
     * 代理人地址
     */
    @ApiModelProperty(value = "代理人地址")
    private String agentAddress;

    /**
     * 变更
     */
    @ApiModelProperty(value = "变更")
    private String changes;
}
