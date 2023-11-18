package com.yiling.mall.category.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.mall.common.request.GoodsRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 编辑分类 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCategoryRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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

    /**
     * 商品集合
     */
    private List<GoodsRequest> goodsList;
}
