package com.yiling.admin.cms.content.vo;

import com.yiling.admin.cms.goods.vo.GoodsInfoVO;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HMCContentVO extends BaseVO {

    @ApiModelProperty(value = "cms_content主键")
    private Long contentId;

    @ApiModelProperty(value = "引用业务线id")
    private Long lineId;

    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "栏目名称")
    private String categoryName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型:1-文章 2-视频")
    private Integer contentType;

    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer topFlag;

    @ApiModelProperty(value = "浏览量")
    private Integer view;

    @ApiModelProperty(value = "栏目列表排序")
    private Integer categoryRank;

    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    @ApiModelProperty(value = "所属医生名称")
    private String docName;

    @ApiModelProperty(value = "状态 1未发布 2发布")
    private Integer status;

    @ApiModelProperty(value = "引用状态 1-引用，2-取消引用")
    private Integer referStatus;

    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建来源 1-运营后台，2-IH后台")
    private Integer createSource;


}
