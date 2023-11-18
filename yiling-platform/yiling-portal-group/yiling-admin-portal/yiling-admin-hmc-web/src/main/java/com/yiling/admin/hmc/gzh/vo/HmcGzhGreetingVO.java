package com.yiling.admin.hmc.gzh.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公众号欢迎语VO
 *
 * @author: fan.shen
 * @data: 2023/03/28
 */
@Data
@Accessors(chain = true)
@ApiModel("公众号欢迎语VO")
public class HmcGzhGreetingVO extends BaseVO {

    /**
     * 场景id
     */
    @ApiModelProperty(value = "场景id")
    private Integer sceneId;

    /**
     * 场景名称
     */
    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    /**
     * 累计触发次数
     */
    @ApiModelProperty(value = "累计触发次数")
    private Long triggerCount;

    /**
     * 发布状态 1-已发布，2-未发布
     */
    @ApiModelProperty(value = "发布状态 1-已发布，2-未发布")
    private Integer publishStatus;

    /**
     * 发布版本
     */
    @ApiModelProperty(value = "发布版本")
    private String publishVersion;

    /**
     * 草稿版本
     */
    @ApiModelProperty(value = "草稿版本")
    private String draftVersion;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    
    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date publishDate;

    /**
     * 备注
     */
    @ApiModelProperty("运营备注")
    private String remark;

}
