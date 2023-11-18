package com.yiling.sjms.form.vo;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.sjms.flee.vo.AppendixDetailVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申诉审批 VO
 *
 * @author: dexi.yao
 * @date: 2023/3/1
 */
@Data
public class ApproveVO {


//    /**
//     * 申诉类型 1 补传月流向 2 调整月流向 3代表终端类型错误 4终端类型申诉 5其他
//     */
//    @ApiModelProperty("【type=10时取申诉类型  2 调整月流向 3代表终端类型错误 4终端类型申诉 5其他】【 type=9时取 申报类型 1-电商 2-非电商】【 type=14时 1-补传月流向】")
//    private Integer appealType;

    @ApiModelProperty("申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货 " +
            "9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误")
    private Integer appealType;

    /**
     * 申诉金额
     */
    @ApiModelProperty("申诉金额")
    private BigDecimal appealAmount;

    /**
     * 附件
     */
    @JsonIgnore
    @ApiModelProperty(value = "附件",hidden = true)
    private String appendix;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<AppendixDetailVO> appendixList;

    /**
     * 申诉描述
     */
    @ApiModelProperty(value = "申诉描述")
    private String describe;

    /**
     * 调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货
     */
    @ApiModelProperty(value = "调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货")
    private Integer monthAppealType;

    /**
     * 确认时的备注意见
     */
    @ApiModelProperty(value = "确认时的备注意见")
    private String confirmDescribe;

    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    @ApiModelProperty(value = "传输类型：1、上传excel; 2、选择流向")
    private Integer transferType;

}
