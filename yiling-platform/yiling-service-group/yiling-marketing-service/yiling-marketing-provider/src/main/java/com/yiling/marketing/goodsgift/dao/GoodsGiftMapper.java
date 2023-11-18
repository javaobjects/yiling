package com.yiling.marketing.goodsgift.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;

/**
 * <p>
 * 赠品信息表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface GoodsGiftMapper extends BaseMapper<GoodsGiftDO> {

    /**
     * 扣减赠品库存数量
     * @param quantity 扣减数量
     * @param id 赠品id
     * @return
     */
    boolean deduct(@Param("quantity")Integer quantity, @Param("id") Long id );

    /**
     * 批量返回赠品库存数量
     * @param quantity 返还数量
     * @param id 赠品id
     * @return
     */
    boolean increase(@Param("quantity")Integer quantity,@Param("id") Long id );

    /**
     * 更新赠品库
     * @param introduction 介绍
     * @return
     */
    void updateIntroduct(@Param("id")Long id, @Param("introduction")String introduction);


    String getIntroductById(@Param("id")Long id);

    boolean saveGoodsGiftDO(GoodsGiftDTO goodsGiftDO);
}
