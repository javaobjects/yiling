package com.yiling.search.flow.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingDTO
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Data
public class EsFlowEnterpriseGoodsMappingDTO extends BaseDTO {

    /**
     * 流向原始名称
     */
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    private String flowSpecification;

    /**
     * 流向原始商品内码
     */
    private String flowGoodsInSn;
    /**
     * 流向原始商品厂家
     */
    private String flowManufacturer;

    /**
     * 流向原始商品厂家
     */
    private String flowUnit;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 标准商品规格
     */
    private String goodsSpecification;

    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 转换单位：1-乘 2-除
     */
    private Integer convertUnit;

    /**
     * 转换系数
     */
    private BigDecimal convertNumber;

    /**
     * 最后上传时间
     */
    private Date lastUploadTime;

    private Date createTime;

    private Long createUser;

    private Date updateTime;

    private Long updateUser;

}
