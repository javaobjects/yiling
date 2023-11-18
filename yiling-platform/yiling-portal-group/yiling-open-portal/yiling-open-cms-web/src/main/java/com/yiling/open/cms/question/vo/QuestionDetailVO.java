package com.yiling.open.cms.question.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionDetailVO extends BaseDTO {

    /**
     * 标题
     */
    @ApiModelProperty("疑义标题")
    private String title;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 所属分类：1-药品相关
     */
    @ApiModelProperty("所属分类：1-药品相关")
    private Integer categoryId;

    /**
     * 内容详情
     */
    @ApiModelProperty("内容详情")
    private String content;

    /**
     * 文献信息
     */
    @ApiModelProperty("文献信息")
    private List<DocumentVO> documentList;

    /**
     * 药品信息集合
     */
    @ApiModelProperty("药品信息集合")
    private List<QuestionStandardGoodsInfoVO> standardGoodsList;

    /**
     * 关联链接
     */
    @ApiModelProperty("链接")
    private List<String> urlList;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;


}
