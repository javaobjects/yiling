package com.yiling.open.cms.content.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.open.cms.goods.vo.StandardGoodsInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientAdminContentVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subtitle;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "封面oss key")
    private String coverKey;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer isTop;

    /**
     * 1-是 0-否
     */
    @ApiModelProperty(value = "1-是 0-否")
    private Integer isDraft;
    /**
     * 状态 1未发布 2发布
     */
    @ApiModelProperty(value = "状态 1未发布 2发布")
    private Integer status;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;


    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(value = "是否公开：0-否 1-是")
    private Integer isOpen;

    @ApiModelProperty(value = "关联商品")
    private List<StandardGoodsInfoVO> standardGoodsList;

    /**
     *
     */
    @ApiModelProperty(value = "科室")
    private List<Integer> deptIdList;


    @ApiModelProperty(value = "疾病")
    private List<Integer> diseaseIdList;

    @ApiModelProperty("医生id")
    private Long docId;

    @ApiModelProperty("栏目信息")
    private List<IHPatientContentCategoryVO> categoryList;
}
