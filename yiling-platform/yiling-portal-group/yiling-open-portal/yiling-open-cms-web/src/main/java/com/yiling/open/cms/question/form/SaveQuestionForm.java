package com.yiling.open.cms.question.form;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
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
     * 问题标题
     */
    @Length(max = 20)
    @NotBlank
    @ApiModelProperty(value = "问题标题",required = true)
    private String title;

    /**
     * 内容详情
     */
    @ApiModelProperty(value = "内容详情",required = true)
    @Length(max = 200)
    @NotBlank
    private String content;


    @ApiModelProperty(value = "图片",required = true)
    private List<String> keyList;


    /**
     * 提问人名称
     */
    @NotBlank
    @ApiModelProperty(value = "提问人名称",required = true)
    private String fromUserName;

    /**
     * 提问人Id
     */
    @NotNull
    @ApiModelProperty(value = "提问人Id",required = true)
    private Long fromUserId;

    /**
     * 提问人Id
     */
    @NotBlank
    @ApiModelProperty(value = "提问人医院名称",required = true)
    private String fromUserHospitalName;


    /**
     * 被提问医药代表名称id
     */
    @ApiModelProperty(value = "绑定医药代表",required = true)
    private List<Long> userIdList;

    /**
     * 关联标准库商品信息
     */
    @ApiModelProperty(value = "商品信息",required = true)
    @Valid
    @NotEmpty
    private List<QuestionStandardGoodsInfoForm> standardInfoList;





}
