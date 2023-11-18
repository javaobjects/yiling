package com.yiling.mall.recommend.bo;

import java.util.Date;
import java.util.List;

import com.yiling.goods.medicine.dto.GoodsInfoDTO;

import lombok.Data;

/**
 * <p>
 * Recommend BO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
public class RecommendBO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * Recommend标题
     */
    private String title;

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
     * 商品信息列表
     */
    private List<GoodsInfoDTO> goodsDTOList;

}
