package com.yiling.b2b.app.common.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BannerAndVajraVO extends BaseVO {

    @ApiModelProperty(value = "金刚位信息")
    private List<B2bAppVajraPositionVO> vajraPositionVOList;

    @ApiModelProperty(value = "主banner信息")
    private List<B2bAppBannerVO> mainBannerVOList;

    @ApiModelProperty(value = "副banner信息")
    private List<B2bAppBannerVO> secondBannerVOList;
}
