package com.yiling.open.erp.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.open.erp.validation.group.Delete;

import lombok.Data;
import lombok.ToString;

/**
 * @author: houjie.sun
 * @date: 2022/4/21
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpShopSaleFlowStringDTO {

    /**
     * 供应商部门
     */
    @ToString.Include
    @JSONField(name = "su_dept_no")
    protected String suDeptNo;

    /**
     * 门店编码
     */
    @ToString.Include
    @JSONField(name = "shop_no")
    protected String shopNo;

    /**
     * 门店名称
     */
    @ToString.Include
    @JSONField(name = "shop_name")
    protected String shopName;

    /**
     * Erp采购订主键
     */
    @JSONField(name = "so_id")
    @ToString.Include
    protected String soId;

    /**
     * Erp采购订单号
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "so_no")
    @ToString.Include
    protected String soNo;

    /**
     * 采购日期
     */
    @NotNull(message = "不能为空")
    @ToString.Include
    private String soTime;

    /**
     * 客户名称
     */
    @JSONField(name = "enterprise_name")
    @ToString.Include
    private String enterpriseName;

    /**
     * 批号
     */
    @JSONField(name = "so_batch_no")
    @ToString.Include
    private String soBatchNo;

    /**
     * 销售数量
     */
    @JSONField(name = "so_quantity",serialzeFeatures = SerializerFeature.WriteBigDecimalAsPlain)
    @ToString.Include
    private BigDecimal soQuantity;

    /**
     * 生产日期
     */
    @ToString.Include
    private String soProductTime;

    /**
     * 效期
     */
    @ToString.Include
    private String soEffectiveTime;

    /**
     * 价格
     */
    @JSONField(name = "so_price")
    @ToString.Include
    private BigDecimal soPrice;

    /**
     * 商品内码
     */
    @NotBlank(message = "不能为空")
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
    @JSONField(name = "so_license")
    @ToString.Include
    private String soLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "so_specifications")
    @ToString.Include
    private String soSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "so_unit")
    @ToString.Include
    private String soUnit;

    /**
     * 商品生产厂家
     */
    @JSONField(name = "so_manufacturer")
    @ToString.Include
    private String soManufacturer;

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
    public ErpShopSaleFlowStringDTO() {
        this.soId = "";
        this.suDeptNo = "";
        this.soNo = "";
        this.enterpriseName = "";
        this.soBatchNo = "";
        this.soQuantity = BigDecimal.ZERO;
        this.soPrice = BigDecimal.ZERO;
        this.goodsInSn = "";
        this.goodsName = "";
        this.soLicense = "";
        this.soSpecifications = "";
        this.soUnit = "";
        this.soManufacturer = "";
        this.cnt = 1;
    }
}
