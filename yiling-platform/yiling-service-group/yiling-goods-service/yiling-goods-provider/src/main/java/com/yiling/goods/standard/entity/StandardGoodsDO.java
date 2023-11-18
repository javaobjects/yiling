package com.yiling.goods.standard.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品标准表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("standard_goods")
public class StandardGoodsDO extends BaseDO  {

    private static final long serialVersionUID = 1L;


    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 商品状态：0正常 1禁用
     */
    private Integer goodsStatus;

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
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 分类id1
     */
    private Long standardCategoryId1;

    /**
     * 分类名称1
     */
    private String standardCategoryName1;

    /**
     * 分类id2
     */
    private Long standardCategoryId2;

    /**
     * 分类名称2
     */
    private String standardCategoryName2;

    /**
     * 商品别名
     */
    private String aliasName;

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer isCn;

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
     * 管制类型：0非管制 1管制
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
     * 特殊成分：0不含麻黄碱 1含麻黄碱
     */
    private Integer specialComposition;

    /**
     * 质量标准类别：1中国药典 2地方标准 3其他
     */
    private Integer qualityType;

    /**
     * 来源
     */
    private String goodsSource;

    /**
     * 含量
     */
    private String roughWeight;

    /**
     * 等级
     */
    private String goodsGrade;

    /**
     * 批准日期
     */
    private String approvalDate;


    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 产品类型
     */
    private String productClassification;

    /**
     * 产品标准号/生产许可证编号
     */
    private String productStandardCode;

    /**
     * 经营范围 1 ：一类医疗器械  2：二类医疗器械 3：三类医疗器械
     */
    private Integer businessScope;

    /**
     * 注册人
     */
    private String registrant;

    /**
     * 注册人地址
     */
    private String registrantAddress;

    /**
     * 代理人
     */
    private String agent;

    /**
     * 代理人地址
     */
    private String agentAddress;

    /**
     * 变更
     */
    private String changes;

    /**
     * 以岭标识 0:非以岭  1：以岭
     */
    private Integer ylFlag;


    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
