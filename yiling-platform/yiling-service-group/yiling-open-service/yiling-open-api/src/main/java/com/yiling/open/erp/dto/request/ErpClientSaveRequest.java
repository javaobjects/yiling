package com.yiling.open.erp.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientSaveRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * crm所属省份编码
     */
    private String crmProvinceCode;

    /**
     * 父类公司ID
     */
    private Long suId;

    /**
     * 分公司标识
     */
    private String suDeptNo;

    /**
     * 公司ID
     */
    private Long rkSuId;

    /**
     * 客户端实例Id
     */
    private String clientKey;

    private String clientSecret;
    /**
     * 终端名称
     */
    private String clientName;

    private String domain;

    /**
     * 终端激活状态（0未激活1已激活）
     */
    private Integer clientStatus;

    /**
     * 同步状态
     */
    private Integer syncStatus;

    /**
     * 对接深度0未对接1一度对接2二度对接3三度对接
     */
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    private Integer flowLevel;

    /**
     * 实施人员
     */
    private String installEmployee;

    /**
     * erp品牌
     */
    private String erpBrand;

    /**
     * 商务人员
     */
    private String businessEmployee;

    /**
     * 技术人员
     */
    private String technologyEmployee;

    /**
     * 技术人员联系方式
     */
    private String technologyEmployeePhone;

    /**
     * 库存读取规则0读取商品库存1读取库存汇总
     */
    private Integer goodsNumberRule;

    private Integer goodsStatusRule;

    private BigDecimal orderTimeRule;

    private Integer command;

    private Integer commandStatus;

    private Date commandTime;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 备注
     */
    private String remark;

}
