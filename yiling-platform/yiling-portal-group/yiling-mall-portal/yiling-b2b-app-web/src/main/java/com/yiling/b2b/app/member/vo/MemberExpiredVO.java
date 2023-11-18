package com.yiling.b2b.app.member.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员到期提醒 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-25
 */
@Data
@Accessors(chain = true)
public class MemberExpiredVO {

    @ApiModelProperty("会员ID")
    private Long memberId;

    @ApiModelProperty("会员名称")
    private String memberName;

    @ApiModelProperty("到期时间")
    private Date expiredDate;

}
