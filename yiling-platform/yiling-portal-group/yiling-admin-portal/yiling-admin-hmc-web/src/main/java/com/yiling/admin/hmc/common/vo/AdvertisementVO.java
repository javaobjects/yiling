package com.yiling.admin.hmc.common.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class AdvertisementVO extends BaseVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("图片地址")
    private String pic;

    @ApiModelProperty("图片地址(完整)")
    private String picUrl;

    @ApiModelProperty("跳转类型 1-h5跳转，2-小程序内部跳转")
    private Integer redirectType;

    @ApiModelProperty("链接地址")
    private String url;

    @ApiModelProperty("有效起始时间")
    private Date startTime;

    @ApiModelProperty("有效截止时间")
    private Date stopTime;

    @ApiModelProperty("投放位置:1-C端用户侧首页 2-C端用户侧我的")
    private List<Integer> position;

    @ApiModelProperty("显示顺序")
    private Integer sort;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;
}
