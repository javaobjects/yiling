package com.yiling.dataflow.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsRelationDTO extends BaseDTO {

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商业商品名称
     */
    private String goodsName;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 商业商品规格
     */
    private String goodsSpecifications;

    /**
     * 生产厂家
     */
    private String goodsManufacturer;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    private Integer goodsRelationLabel;

    private String goodsRelationLabelStr;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人id
     */
    private Long opUserId;

    /**
     * 操作时间
     */
    private Date opTime;

}
