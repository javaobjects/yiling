package com.yiling.sjms.wash.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockAreaRecordVO extends BaseVO {

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    @ApiModelProperty(value = "非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构")
    private Integer customerClassification;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    /**
     * 品种名字
     */
    @ApiModelProperty(value = "品种名字")
    private String categoryName;

    /**
     * 销量计入类型：1-销量计入主管 2-销量计入代表
     */
    @ApiModelProperty(value = "销量计入类型：1-销量计入主管 2-销量计入代表")
    private Integer type;

    /**
     * 代表岗位代码
     */
    @ApiModelProperty(value = "代表岗位代码")
    private String representativePostCode;

    /**
     * 代表岗位名称
     */
    @ApiModelProperty(value = "代表岗位名称")
    private String representativePostName;

    /**
     * 代表工号
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "代表姓名")
    private String representativeName;

    /**
     * 主管岗位代码
     */
    @ApiModelProperty(value = "主管岗位代码")
    private String executivePostCode;

    /**
     * 主管岗位名称
     */
    @ApiModelProperty(value = "主管岗位名称")
    private String executivePostName;

    /**
     * 主管工号
     */
    @ApiModelProperty(value = "主管工号")
    private String executiveCode;

    /**
     * 主管姓名
     */
    @ApiModelProperty(value = "主管姓名")
    private String executiveName;

    /**
     * 业务部门
     */
    @ApiModelProperty(value = "业务部门")
    private String department;

    /**
     * 业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String province;

    /**
     * 业务区域
     */
    @ApiModelProperty(value = "业务区域")
    private String area;

    /**
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long lastOpUser;

    @ApiModelProperty(value = "操作人姓名")
    private String lastOpUserName;

    @ApiModelProperty(value = "备注")
    private String remark;
}
