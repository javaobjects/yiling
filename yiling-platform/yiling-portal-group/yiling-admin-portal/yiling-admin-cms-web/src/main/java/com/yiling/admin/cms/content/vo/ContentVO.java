package com.yiling.admin.cms.content.vo;

import java.util.Date;
import java.util.List;

import com.yiling.admin.cms.goods.vo.GoodsInfoVO;
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


    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "栏目名称")
    private Long categoryName;

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
     * 内容来源:1-站内创建 2-外链
     */
    @ApiModelProperty(value = "内容来源:1-站内创建 2-外链")
    private Integer sourceContentType;

    /**
     * H5地址
     */
    @ApiModelProperty(value = "H5地址")
    private String linkUrl;

    /**
     * 置顶:1-是 0--否
     */
    @ApiModelProperty(value = "置顶:1-是 0--否")
    private Integer isTop;

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量")
    private Integer pageView;

    /**
     * 2C用户侧浏览量
     */
    @ApiModelProperty(value = "2C用户侧浏览量")
    private Integer hmcView;

    /**
     * 医生端浏览量
     */
    @ApiModelProperty(value = "医生端浏览量")
    private Integer ihDocView;

    /**
     * IH患者端浏览量
     */
    @ApiModelProperty(value = "IH患者端浏览量")
    private Integer ihPatientView;

    /**
     * 销售助手端浏览量
     */
    @ApiModelProperty(value = "销售助手端浏览量")
    private Integer saView;

    /**
     * 大运河端浏览量
     */
    @ApiModelProperty(value = "大运河端浏览量")
    private Integer b2bView;

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
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "显示产品线")
    private List<Long> displayLines;


    private Long createUser;


    /**
     * 修改人
     */
    private Long updateUser;


    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty(value = "是否公开：0-否 1-是")
    private Integer isOpen;

    /**
     * 视频oss key
     */
    @ApiModelProperty(value = "视频url")
    private String vedioFileUrl;

    @ApiModelProperty(value = "视频oss key")
    private String vedioFileUrlKey;


    /**
     * 会议id
     */
    @ApiModelProperty(value = "会议id")
    private Long meetingId;

    @ApiModelProperty(value = "会议名称")
    private String meetingName;

    /**
     * 科室
     */
    @ApiModelProperty(value = "疾病id列表")
    private List<Integer> deptIdList;
    /**
     * 疾病
     */
    @ApiModelProperty(hidden = true)
    private List<Integer> diseaseIdList;

    @ApiModelProperty(value = "疾病")
    private List<DiseaseVO> diseaseVOList;

    @ApiModelProperty(value = "主讲人")
    private String speaker;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    @ApiModelProperty(value = "关联商品")
    private List<GoodsInfoVO> standardGoodsList;

    @ApiModelProperty(value = "科室")
    private List<HospitalDeptVO> hospitalDeptVOS;

    @ApiModelProperty(value = "内容权限 1-仅登录 2-需认证通过")
    private Integer viewLimit;

    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    @ApiModelProperty(value = "所属医生名称")
    private String docName;

    /**
     * 所在医疗机构
     */
    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    /**
     * 所在医疗机构科室
     */
    @ApiModelProperty(value = "所在医疗机构科室")
    private String hospitalDepartment;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    @ApiModelProperty(value = "创建来源 1-运营后台，2-IH后台")
    private Integer createSource;

    /**
     * 是否被引用 0-否，1-是
     */
    @ApiModelProperty(value = "是否被引用 0-否，1-是")
    private Integer useFlag = 0;

}
