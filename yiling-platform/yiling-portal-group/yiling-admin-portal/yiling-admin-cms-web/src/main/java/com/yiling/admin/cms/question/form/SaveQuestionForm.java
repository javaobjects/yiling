package com.yiling.admin.cms.question.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.cms.question.dto.request.QuestionStandardGoodsInfoRequest;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @describe 保存类型为1(问题库区分:1-问题知识库 2-医药代表社区库)的问题
 * @author:wei.wang
 * @date:2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveQuestionForm extends BaseForm {

	private static final long serialVersionUID = -4484861173272278875L;

	/**
     * id
	 */
    @ApiModelProperty("ID")
	private Long id;
    /**
     * 问题标题
     */
    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "问题标题",required = true)
    private String title;

    /**
     * 内容详情
     */
    @ApiModelProperty(value = "内容详情",required = true)
    @NotBlank
    private String content;


    /**
     * 所属分类：1-药品相关
     */
    @NotNull
    @ApiModelProperty(value = "所属分类",required = true)
    private Integer categoryId;

    /**
     * 关联文献id集合
     */
    @ApiModelProperty(value = "关联文献id集合")
    private List<Long> documentIdList;

    /**
     * 关联标准库商品信息
     */
    @NotEmpty
    @ApiModelProperty(value = "关联标准库商品信息",required = true)
    private List<QuestionStandardGoodsInfoForm> standardInfoList;

    /**
     *描述
     */
    @NotBlank
    @Length(max = 100)
    @ApiModelProperty(value = "描述",required = true)
    private String description;
}
