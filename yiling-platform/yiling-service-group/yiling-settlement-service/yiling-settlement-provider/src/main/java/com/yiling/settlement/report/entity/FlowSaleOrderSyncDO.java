package com.yiling.settlement.report.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向销售明细信息同步表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_flow_sale_order_sync")
public class FlowSaleOrderSyncDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 流向表id
     */
    private Long flowId;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 有效期
     */
    private Date soEffectiveTime;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 价格
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 订单来源，字典：erp_sale_flow_source
     */
    private String soSource;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * yl_goods_id最近修改时间
     */
    private Date ylGoodsIdUpdateTime;

    /**
     * 老yl_goods_id
     */
    private Long ylGoodsIdOld;

    /**
     * 报表ID
     */
    private Long reportId;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    private String abnormalDescribed;

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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        FlowSaleOrderSyncDO syncDO = (FlowSaleOrderSyncDO) o;
        return flowId.equals(syncDO.flowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flowId);
    }
}
