package com.yiling.f2b.admin.agreement.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补充协议快照明细列表
 * @author: houjie.sun
 * @date: 2021/8/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementsCategoryVO extends BaseVO {

    /**
     * 协议主体ID(甲方)
     */
    @ApiModelProperty(value = "协议主体ID(甲方)")
    private Long                     eid;

    /**
     * 协议主体名称(甲方)
     */
    @ApiModelProperty(value = "协议主体名称(甲方)")
    private String                   ename;

    /**
     * 年度协议ID
     */
    @ApiModelProperty(value = "年度协议ID")
    private Long                     parentId;

    /**
     * 年度协议名称
     */
    @ApiModelProperty(value = "年度协议名称")
    private String                   parentName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date                     updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long                     updateUser;

    /**
     * 修改日志详情
     */
    @ApiModelProperty(value = "修改日志详情")
    List<AgreementsSnapshotDetailVO> agreementsSnapshotList;

}
