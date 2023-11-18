package com.yiling.cms.content.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class UpdateContentRequest extends BaseRequest {

    private Long id;

    private static final long serialVersionUID = 4317470651991777610L;
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 封面
     */
    private String cover;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容来源:1-站内创建 2-外链
     */
    private Integer sourceContentType;

    /**
     * H5地址
     */
    private String linkUrl;

    /**
     * 置顶:1-是 0--否
     */
    private Integer isTop;

    /**
     * 状态 1未发布 2发布
     */
    private Integer status;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 1-是 0-否
     */
    private Integer isDraft;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishTime;

    // private List<Long> displayLines;


    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     * 会议id
     */
    private Long meetingId;

    /**
     * 关联标准库商品
     */
    private List<Long> standardGoodsIdList;
    /**
     * 科室
     */
    private List<Integer> deptIdList;
    /**
     * 疾病
     */
    private List<Integer> diseaseIdList;

    private String speaker;

    private Integer viewLimit;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 业务线模块设置
     */
    List<LineModuleRequest> lineModuleList;
}
