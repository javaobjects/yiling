package com.yiling.sjms.gb.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GbFormDetailVO extends BaseVO {
    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 来源团购编号
     */
    @ApiModelProperty(value = "来源团购编号")
    private String srcGbNo;


    /**
     * 业务类型：1-提报 2-取消
     */
    @ApiModelProperty(value = "所属流程：1-提报 2-取消 3-费用申请")
    private Integer bizType;


    /**
     * 所属流程ID
     */
    @ApiModelProperty(value = "所属流程ID")
    private String flowId;

    /**
     * 所属流程名称
     */
    @ApiModelProperty(value = "所属流程名称")
    private String flowName;

    /**
     * 发起人ID
     */
    @ApiModelProperty(value = "发起人ID")
    private String empId;

    /**
     * 发起人姓名
     */
    @ApiModelProperty(value = "发起人姓名")
    private String empName;

    /**
     * 发起人部门ID
     */
    @ApiModelProperty(value = "发起人部门ID")
    private String deptId;

    /**
     * 发起人部门名称
     */
    @ApiModelProperty(value = "发起人部门名称")
    private String deptName;

    /**
     * 提交审批时间
     */
    @ApiModelProperty(value = "提交审批时间")
    private Date submitTime;

    /**
     * 审批通过时间
     */
    @ApiModelProperty(value = "审批通过时间")
    private Date approveTime;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     *
     */
    @ApiModelProperty(value = "状态：10-待提交 20-审批中 200-已通过 201-已驳回")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     *基本信息
     */
    @ApiModelProperty(value = "基本信息")
    private GbBaseInfoVO baseInfo;

    /**
     *基本信息
     */
    @ApiModelProperty(value = "团购信息")
    private GbMainInfoVO mainInfo;

    /**
     *文件信息
     */
    @ApiModelProperty(value = "文件信息")
    private List<FileNameInfoVO> fileInfoList;

    /**
     *团购出库终端和商业信息
     */
    @ApiModelProperty(value = "团购出库终端和商业信息")
    private List<GbCompanyInfoVO> companyInfoList;

    @ApiModelProperty(value = "审批记录")
    private List<GbProcessDetailVO> processDetailList;

    /**
     * 是否复核: 1-否 2-是
     */
    @ApiModelProperty(value = "复核状态 1-否 2-是")
    private Integer reviewStatus;

    /**
     * 是否复核: 1-否 2-是
     */
    @ApiModelProperty(value = "复核状态意见")
    private String reviewReply;

    /**
     * 是否复核: 1-否 2-是
     */
    @ApiModelProperty(value = "复核时间")
    private Date reviewTime;

    /**
     *团购费用申请文件信息
     */
    @ApiModelProperty(value = "团购费用申请文件信息")
    private List<FileNameInfoVO> feeApplicationFileInfoList;

    /**
     * 团购费用申请原因
     */
    @ApiModelProperty(value = "团购费用申请原因")
    private String feeApplicationReply;
}
