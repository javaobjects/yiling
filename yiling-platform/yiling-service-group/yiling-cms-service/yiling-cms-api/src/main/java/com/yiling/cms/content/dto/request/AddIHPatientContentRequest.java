package com.yiling.cms.content.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIHPatientContentRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

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
     * 置顶:1-是 0--否
     */
    private Integer isTop;

    /**
     * 状态 1未发布 2发布
     */
    private Integer status;

    /**
     * 1-是 0-否
     */
    private Integer isDraft;

    /**
     * 内容
     */
    private String content;


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
    private List<Long> deptIdList;

    /**
     * 疾病
     */
    private List<Long> diseaseIdList;

    /**
     * 主讲人
     */
    private String speaker;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    private Integer createSource;

    /**
     * 栏目集合
     */
    private List<AddOrUpdateIHPatientContentCategoryRequest> categoryList;
}
