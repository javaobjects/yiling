package com.yiling.admin.cms.content.form;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.framework.common.base.form.QueryPageListForm;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.system.bo.Staff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 内容分页列表查询参数
 *
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryContentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "引用业务线id")
    private Long lineId;

    @ApiModelProperty(value = "板块id")
    private Long moduleId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "医生id")
    private Long docId;

    @ApiModelProperty(value = "状态：1-未发布 2-已发布")
    private Integer status;

    @ApiModelProperty(value = "是否院方文章 0-否，1-是")
    private Integer ihFlag;

    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer isTop;

    @ApiModelProperty(value = "类型:1-文章 2-视频")
    @NotNull
    private Integer contentType;

    @ApiModelProperty(value = "创建来源 1-运营后台，2-IH后台")
    private Integer createSource;

    /**
     * 内容来源: 0-所有 1-站内创建 2-外链
     */
    @ApiModelProperty(value = "内容来源: 0-所有 1-站内创建 2-外链")
    private Integer sourceContentType;

    /**
     * 参数校验
     */
    public void checkParam() {
        // 创建人名称不为空 && 创建来源为空 -> 提示选择创建来源
        if (StrUtil.isNotBlank(this.getCreateUserName()) && Objects.isNull(this.getCreateSource())) {
            throw new BusinessException(CmsErrorCode.PLEASE_SELECT_CREATE_SOURCE);
        }

    }

}