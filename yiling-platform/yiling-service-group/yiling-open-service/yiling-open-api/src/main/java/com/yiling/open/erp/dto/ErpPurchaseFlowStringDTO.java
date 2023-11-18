package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;
import com.yiling.open.erp.validation.group.Delete;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import lombok.ToString;

/**
 * @author: houjie.sun
 * @date: 2022/4/21
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpPurchaseFlowStringDTO {

    /**
     * 供应商部门
     */
    @ToString.Include
    @JSONField(name = "su_dept_no")
    protected String suDeptNo;

    /**
     * Erp采购订主键
     */
    @JSONField(name = "po_id")
    @ToString.Include
    private String poId;

    /**
     * Erp采购订单号
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "po_no")
    @ToString.Include
    protected String poNo;

    /**
     * 采购日期
     */
    @NotNull
    @ToString.Include
    private String poTime;

    /**
     * 下单日期
     */
    @ToString.Include
    private String orderTime;

    /**
     * 客户内码
     */
    @JSONField(name = "enterprise_inner_code")
    @ToString.Include
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    @JSONField(name = "enterprise_name")
    @ToString.Include
    private String enterpriseName;

    /**
     * 批号
     */
    @JSONField(name = "po_batch_no")
    @ToString.Include
    private String poBatchNo;

    /**
     * 采购数量
     */
    @JSONField(name = "po_quantity",serialzeFeatures = SerializerFeature.WriteBigDecimalAsPlain)
    @ToString.Include
    private BigDecimal poQuantity;

    /**
     * 生产日期
     */
    @ToString.Include
    private String poProductTime;

    /**
     * 效期
     */
    @ToString.Include
    private String poEffectiveTime;

    /**
     * 价格
     */
    @JSONField(name = "po_price")
    @ToString.Include
    private BigDecimal poPrice;

    /**
     * 商品内码
     */
    @JSONField(name = "goods_in_sn")
    @ToString.Include
    private String goodsInSn;

    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    @ToString.Include
    private String goodsName;

    /**
     * 批准文号
     */
    @JSONField(name = "po_license")
    @ToString.Include
    private String poLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "po_specifications")
    @ToString.Include
    private String poSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "po_unit")
    @ToString.Include
    private String poUnit;

    /**
     * 商品生产厂家
     */
    @JSONField(name = "po_manufacturer")
    @ToString.Include
    private String poManufacturer;

    /**
     * 数据来源
     */
    @JSONField(name = "po_source")
    @ToString.Include
    private String poSource;

    /**
     * 操作类型，1新增，2修改，3删除
     */
    @NotNull(message="不能为空", groups = Delete.class)
    @JSONField(name = "oper_type")
    protected Integer operType;

    private Integer cnt;

    /**
     * 初始化数据
     */
    public ErpPurchaseFlowStringDTO() {
        this.poId = "";
        this.suDeptNo = "";
        this.poNo = "";
        this.enterpriseInnerCode = "";
        this.enterpriseName = "";
        this.poBatchNo = "";
        this.poQuantity = BigDecimal.ZERO;
        this.poPrice = BigDecimal.ZERO;
        this.goodsInSn = "";
        this.goodsName = "";
        this.poLicense = "";
        this.poSpecifications = "";
        this.poUnit = "";
        this.poManufacturer = "";
        this.poSource = "";
        this.cnt = 1;
    }

}
