package com.yiling.open.cms.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
     * 发布时间
     */
    private Date publishTime;

    private Date createTime;

    private String speaker;

    private String contentType;

    private String vedioFileUrl;

    /**
     * 业务线id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 内容权限 1-仅登录，2-需认证
     */
    private Integer contentAuth;

    /**
     * 内容id
     */
    private Long contentId;

}
