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
public class ContentVO extends BaseVO {

    /**
     * 所属医生id
     */
    private Long contentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 点击量
     */
    @ApiModelProperty("2C用户侧浏览量")
    private Integer hmcView;

    /**
     * 发布时间
     */
    private Date publishTime;

    private Date updateTime;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 医生信息
     */
    private HmcDoctorInfoVO doctorInfoVo = new HmcDoctorInfoVO();

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    private Date createTime;

}
