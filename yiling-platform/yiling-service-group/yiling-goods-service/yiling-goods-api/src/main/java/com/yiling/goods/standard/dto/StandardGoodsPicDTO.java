package com.yiling.goods.standard.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsPicDTO extends BaseDTO {

    /**
     * 标准库商品ID
     */
    private Long standardId;

    /**
     * 标准商品规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品图片值
     */
    private String pic;

    /**
     * 图片路径
     */
    private String picUrl;

    /**
     * 图片排序
     */
    private Integer picOrder;

    /**
     * 是否商品默认图片（0否1是）
     */
    private Integer picDefault;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */

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


}
