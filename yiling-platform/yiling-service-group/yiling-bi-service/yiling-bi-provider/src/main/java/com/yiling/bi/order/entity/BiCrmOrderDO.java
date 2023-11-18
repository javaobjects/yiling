package com.yiling.bi.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * crm流向数据表
 * </p>
 *
 * @author baifc
 * @since 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bi_crm_order")
public class BiCrmOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 销售年份
     */
    private Integer soYear;

    /**
     * 销售月份
     */
    private String soMonth;

    /**
     * 配送商业代码
     */
    private String sellerEcode;

    /**
     * 配送商业id
     */
    private Long sellerEid;

    /**
     * 配送商业名称
     */
    private String sellerEname;

    /**
     * 流向客户名称
     */
    private String buyerEname;

    /**
     * 终端/商业省区
     */
    private String terminalArea;

    /**
     * 终端/商业部门
     */
    private String terminalDept;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 主管工号
     */
    private String satrapNo;

    /**
     * 主管姓名
     */
    private String satrapName;

    /**
     * 代表工号
     */
    private String representativeNo;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 供应链角色
     */
    private String chainRole;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 品种
     */
    private String variety;

    /**
     * 产品代码
     */
    private Long goodsId;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 产品规格
     */
    private String goodsSpec;

    /**
     * 产品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 商销量
     */
    private BigDecimal salesVolume;

    /**
     * 规格
     */
    private String spec;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 流向类型
     */
    private String flowType;

    /**
     * 当月非锁判断
     */
    private String lockFlag;

    /**
     * 判断备注
     */
    private String remark;

    /**
     * 是否已匹配 0-未匹配 1-已匹配
     */
    private Integer matchFlag;

    /**
     * 订单编号，多个以逗号隔开
     */
    private String orderNo;

    /**
     * 匹配说明 0-1对1 1-1对多 2-多对多（暂不支持多对多）
     */
    private Integer matchType;

    /**
     * 购进渠道 0-大运河 1-京东
     */
    private Integer purchaseChannel;

    /**
     * 销售渠道 0-大运河 1-自建渠道
     */
    private Integer salesChannel;

    /**
     * 是否有效 0-有效 1-库存不足
     */
    private Integer effectiveFlag;

    /**
     * 单据类型 0-原始单据 1-系统生成
     */
    private Integer orderType;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;


}
