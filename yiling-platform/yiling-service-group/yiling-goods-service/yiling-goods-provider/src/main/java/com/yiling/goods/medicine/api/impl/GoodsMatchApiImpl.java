package com.yiling.goods.medicine.api.impl;

import java.util.List;

import com.yiling.goods.medicine.api.GoodsMatchApi;
import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchNameDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;
import com.yiling.goods.medicine.service.GoodsMatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 匹配算法
 *
 * @author shuan
 */
@Slf4j
@DubboService
public class GoodsMatchApiImpl implements GoodsMatchApi {

    @Autowired
    private GoodsMatchService goodsMatchService;

    /**
     * 普通字符串进行比较匹配
     *
     * @param orgName 原数据
     * @param targets 待匹配的列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchNameDTO matchingName(String orgName, List<MatchNameDTO> targets) {
        return goodsMatchService.matchingName(orgName, targets);
    }


    /**
     * 进行商品信息的匹配，比较商品规则
     *
     * @param matchGoods 原商品数据
     * @param targets    待匹配的商品列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchedGoodsDTO matchingGoodsWithSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        return goodsMatchService.matchingGoodsWithSpec(matchGoods, targets);
    }

    @Override
    public MatchedGoodsDTO matchingGoodsWithNameAndSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        return goodsMatchService.matchingGoodsWithNameAndSpec(matchGoods,targets);
    }

    /**
     * 进行商品信息的匹配，不比較商品規格
     *
     * @param matchGoods 原商品数据
     * @param targets    待匹配的商品列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchedGoodsDTO matching(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        return goodsMatchService.matching(matchGoods, targets);
    }

}
