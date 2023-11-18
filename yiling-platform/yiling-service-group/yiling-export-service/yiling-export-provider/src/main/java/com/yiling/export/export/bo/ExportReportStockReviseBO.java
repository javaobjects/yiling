package com.yiling.export.export.bo;

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
 *
 * @author dexi.yao
 * @date 2022-09-14
 */
@Data
@Accessors(chain = true)
public class ExportReportStockReviseBO {


    /**
     * 企业id
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 以岭品id
     */
    private Long ylGoodsId;

    /**
     * 以岭产品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecifications;

    /**
     * 调整数量
     */
    private Long poQuantity;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private Integer poSource;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private String poSourceStr;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    private String opUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private String createTimeStr;


}
