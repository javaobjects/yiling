package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.validation.group.Delete;
import com.yiling.open.erp.validation.group.InterFaceValidation;

import lombok.Data;
import lombok.ToString;

/**
 * @author: houjie.sun
 * @date: 2022/4/21
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpGoodsBatchFlowStringDTO {

    /**
     * 供应商部门
     */
    @ToString.Include
    @JSONField(name = "su_dept_no")
    protected String suDeptNo;

    /**
     * Erp库存流水ID主键
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "gb_id_no")
    @ToString.Include
    private String gbIdNo;

    /**
     * 入库时间
     */
    @NotBlank(message = "不能为空",groups = InterFaceValidation.class)
    @JSONField(name = "gb_time")
    @ToString.Include
    private String gbTime;

    /**
     * 商品内码
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "in_sn")
    @ToString.Include
    private String inSn;

    /**
     * 批次号
     */
    @JSONField(name = "gb_batch_no")
    @ToString.Include
    private String gbBatchNo;

    /**
     * 生产日期
     */
    @JSONField(name = "gb_produce_time")
    @ToString.Include
    private String gbProduceTime;

    /**
     * 效期
     */
    @JSONField(name = "gb_end_time")
    @ToString.Include
    private String gbEndTime;

    /**
     * 商品名称
     */
    @JSONField(name = "gb_name")
    @ToString.Include
    private String gbName;

    /**
     * 批准文号
     */
    @JSONField(name = "gb_license")
    @ToString.Include
    private String gbLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "gb_specifications")
    @ToString.Include
    private String gbSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "gb_unit")
    @ToString.Include
    private String gbUnit;

    /**
     * 库存数量
     */
    @JSONField(name = "gb_number")
    @ToString.Include
    private BigDecimal gbNumber;

    /**
     * 生产厂家
     */
    @JSONField(name = "gb_manufacturer")
    @ToString.Include
    private String gbManufacturer;

    /**
     * 生产地址
     */
    @JSONField(name = "gb_produce_address")
    @ToString.Include
    private String gbProduceAddress;

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
    public ErpGoodsBatchFlowStringDTO() {
        this.suDeptNo = "";
        this.inSn = "";
        this.gbBatchNo = "";
        this.gbName = "";
        this.gbLicense = "";
        this.gbSpecifications = "";
        this.gbUnit = "";
        this.gbNumber = BigDecimal.ZERO;
        this.gbManufacturer = "";
        this.gbProduceAddress = "";
        this.gbProduceAddress = "";
        this.cnt = 1;
    }
}
