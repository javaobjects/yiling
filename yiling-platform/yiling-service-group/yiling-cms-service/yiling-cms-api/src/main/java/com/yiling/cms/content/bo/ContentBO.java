package com.yiling.cms.content.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 内容BO
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-02
 */
@Data
@Accessors(chain = true)
public class ContentBO implements java.io.Serializable {

    private static final long serialVersionUID = -7631547950698054406L;

    /**
     * ID
     */
    private Long id;

    private Long categoryId;

    private Long categoryName;

    /**
     * 栏目排序
     */
    private Integer categoryRank;

    /**
     * 精选排序
     */
    private Integer choseRank;

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
     * 点击量
     */
    private Integer pageView;

    /**
     * 2C用户侧浏览量
     */
    private Integer hmcView;

    /**
     * 医生端浏览量
     */
    private Integer ihDocView;

    /**
     * 1-是 0-否
     */
    private Integer isDraft;
    /**
     * 状态 1未发布 2发布
     */
    private Integer status;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishTime;


    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    private List<Long> displayLines;

    /**
     * 关联标准库商品
     */
    List<Long> standardGoodsIdList;

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
     * 科室
     */
    private List<Integer> deptIdList;
    /**
     * 疾病
     */
    private List<Integer> diseaseIdList;

    private String speaker;

    private String meetingName;

    private Integer viewLimit;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 是否院方文章 0-否，1-是
     */
    private Integer ihFlag;

    /**
     * 模块Id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 是否置顶 0-否，1-是
     */
    private Integer topFlag;

}
