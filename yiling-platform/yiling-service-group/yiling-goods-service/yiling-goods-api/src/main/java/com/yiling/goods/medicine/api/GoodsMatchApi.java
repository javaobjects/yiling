package com.yiling.goods.medicine.api;

import java.util.List;

import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchNameDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/7/26
 */
public interface GoodsMatchApi {

     MatchNameDTO matchingName(String orgName, List<MatchNameDTO> targets);

     MatchedGoodsDTO matchingGoodsWithSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets);

    MatchedGoodsDTO matchingGoodsWithNameAndSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets);

     MatchedGoodsDTO matching(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets);
}
