package com.yiling.hmc.cms.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class ContentDetailVO extends BaseVO {

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 点击量
     */
    private Integer hmcView;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String content;

    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    /**
     * 封面
     */
    private String cover;


    /**
     * 主讲人
     */
    @ApiModelProperty(value = "主讲人")
    private String speaker;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 医生信息
     */
    @ApiModelProperty(value = "医生信息")
    private HmcDoctorInfoVO doctorInfoVo = new HmcDoctorInfoVO();

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    /**
     * 点赞标志：1-点赞，2-取消点赞或未点赞
     */
    @ApiModelProperty(value = "点赞标志：1-点赞，2-取消点赞或未点赞")
    private Integer likeStatus = 2;

    /**
     * 医生是否开启问诊服务项 true - 已开通，可以跳转；false - 未开通，不能跳转
     */
    @ApiModelProperty(value = "医生是否开启问诊服务项 true - 已开通，可以跳转；false - 未开通，不能跳转")
    private Boolean checkDoctorDiagnosis;

    @ApiModelProperty(value = "问答数量")
    private Integer qaCount;

}
