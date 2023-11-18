package com.yiling.dataflow.flowcollect.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销量申诉源流向与负流向关联表
 * </p>
 *
 * @author xinxuan.jia
 * @date 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sale_appeal_flow_relate")
public class SaleAppealFlowRelateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 选择申诉流向数据id
     */
    private Long originFlowId;

    /**
     * 源流向key
     */
    private String oldFlowKey;

    /**
     * 新流向key
     */
    private String flowKey;

    /**
     * 销售日期
     */
    private Date saleTime;

    /**
     * 流向类型：1、正常；2、销量申诉；3、窜货申报；4、团购处理；5、补传月流向；6、院外药店
     */
    private Integer flowClassify;

    /**
     * 申诉类型：4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
     */
    private Integer complainType;

    /**
     * 实际年份 实际流向产生的年份
     */
    private Integer realYear;

    /**
     * 实际月份 实际流向产生的月份
     */
    private Integer realMonth;

    /**
     * 计入年份 流向审核通过的年份
     */
    private Integer includedYear;

    /**
     * 计入月份 流向审核通过的月份
     */
    private Integer includedMonth;

    /**
     * 清洗时间 即负流向产生的创建时间
     */
    private Date washTime;

    /**
     * 申诉数量
     */
    private BigDecimal qty;

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


}
