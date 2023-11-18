package com.yiling.f2b.admin.agreement.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补充协议快照详情
 * @author: houjie.sun
 * @date: 2021/8/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementsSnapshotDetailVO extends BaseVO {

    /**
     * 协议ID
     */
    @ApiModelProperty(value = "协议ID")
    private Long agreementId;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private Long version;

}
