package com.yiling.admin.b2b.common.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-开屏位表 列表项VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenPositionItemVO extends BaseVO {

    /**
     * 编号
     */
    @ApiModelProperty("ID编码")
    private String no;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 状态：1-未发布 2-已发布
     */
    @ApiModelProperty("标题")
    private Integer status;

    /**
     * 图片
     */
    @ApiModelProperty("图片Key")
    private String picture;

    /**
     * 图片Url
     */
    @ApiModelProperty("图片Url")
    private String pictureUrl;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人", hidden = true)
    private Long updateUser;

    /**
     * 最后修改人名称
     */
    @ApiModelProperty("最后修改人名称")
    private String updateUserName;

    /**
     * 最后发布时间
     */
    @ApiModelProperty("最后发布时间")
    private Date updateTime;

}
