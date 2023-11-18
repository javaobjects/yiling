package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleRuleVO extends BaseVO {

    /**
     * 规则编码
     */
    @ApiModelProperty("规则编码")
    private String code;
    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String name;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    private Integer sort;

    /**
     * 来源：1-手动设置2-小三批备案3-区域备案4-集采明细
     */
    @ApiModelProperty("来源：1-手动设置2-小三批备案3-区域备案4-集采明细")
    private Integer source;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    @ApiModelProperty("判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构")
    private Integer judgment;

    /**
     * 商业公司范围：1-全部2-指定商业公司
     */
    @ApiModelProperty("商业公司范围：1-全部2-指定商业公司")
    private Integer businessRange;

    @ApiModelProperty("指定商业公司数量")
    private Integer businessNumber;
    /**
     * 客户公司范围：1-全部2-指定客户公司3-指定范围
     */
    @ApiModelProperty("客户公司范围：1-全部2-指定客户公司3-指定范围")
    private Integer customerRange;

    @ApiModelProperty("指定客户公司数量")
    private Integer customerNumber;

    /**
     * 商品范围：1-全部2-指定品种3-指定标签4-指定品规
     */
    @ApiModelProperty("商品范围：1-全部2-指定品种3-指定标签4-指定品规")
    private Integer goodsRange;

    @ApiModelProperty("指定品种数量")
    private Integer goodsCategoryNumber;

    @ApiModelProperty("指定品规数量")
    private Integer goodsNumber;

    /**
     * 销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人
     */
    @ApiModelProperty("销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人")
    private Integer saleRange;

    /**
     * 非锁分配备注
     */
    @ApiModelProperty("非锁分配备注")
    private String notes;

    /**
     * 状态：0-开启1-关闭
     */
    @ApiModelProperty("非锁分配备注")
    private Integer status;


    @ApiModelProperty("指定范围对象")
    private UnlockSaleCustomerRangeVO unlockSaleCustomerRange;

    @ApiModelProperty("指定部门对象")
    private UnlockSaleDepartmentVO unlockSaleDepartment;

    /**
     * 是否系统内置：0-不是1-系统内置
     */
    @ApiModelProperty("是否系统内置：0-不是1-系统内置")
    private Integer isSystem;

    @ApiModelProperty("操作人")
    private String updateUserName;

    @ApiModelProperty("最后操作时间")
    private Date updateTime;
    private Long updateUser;


}
