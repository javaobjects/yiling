package com.yiling.mall.recommend.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.mall.common.request.GoodsRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存推荐位 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRecommendRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 推荐位标题
     */
    private String title;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

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
