package com.yiling.bi.goods.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品关系表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-20
 */
@Data
public class InputGoodsRelationShipDTO  implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品分类
     */
    private String goodsType;

    /**
     * 品种
     */
    private String breed;

    /**
     * 物料编码
     */
    private String wlCode;

    /**
     * crm产品编码
     */
    private String crmGoodsid;

    /**
     * B2B产品id
     */
    private String b2bGoodsid;

    /**
     * B2B规格id
     */
    private String b2bSpecid;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 规格型号
     */
    private String goodsSpec;

    /**
     * 商销价
     */
    private BigDecimal sxPrice;

    /**
     * 供货价
     */
    private BigDecimal ghPrice;

    /**
     * 挂网价
     */
    private BigDecimal gwPrice;

    /**
     * 模板应用标识：1 数拓  2 22年零售  3  23年零售
     */
    private String applicationId;

    /**
     * 零售商品规格
     */
    private String lsSpec;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 协议类别
     */
    private String xyType;

    /**
     * 是连花清瘟配额产品的品种
     */
    private String sfLhpe;

    /**
     * 产品分类
     */
    private String tthreeGoodsSpec;

    /**
     * 协议核算价
     */
    private BigDecimal tthreeGoodsHsj;

    /**
     * 建议零售价
     */
    private BigDecimal tthreeGoodsLsj;


}
