package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.GoodsPicMapper;
import com.yiling.goods.medicine.dto.request.SaveGoodsPicRequest;
import com.yiling.goods.medicine.entity.GoodsPicDO;
import com.yiling.goods.medicine.service.GoodsPicService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 商品图片表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-15
 */
@Service
public class GoodsPicServiceImpl extends BaseServiceImpl<GoodsPicMapper, GoodsPicDO> implements GoodsPicService {

    @Override
    public Boolean insertGoodsPic(List<SaveGoodsPicRequest> saveGoodsPicRequestList,Long goodsId) {
        saveGoodsPicRequestList.forEach(e->{
            e.setGoodsId(goodsId);
        });
        QueryWrapper<GoodsPicDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsPicDO::getGoodsId, goodsId);
        List<GoodsPicDO> goodsPicDOList = this.list(queryWrapper);
        List<Long> picIds = goodsPicDOList.stream().map(e -> e.getId()).collect(Collectors.toList());

        List<SaveGoodsPicRequest> updatePicList = saveGoodsPicRequestList.stream().filter(e -> e.getId() != null && e.getId() != 0).collect(Collectors.toList());
        List<SaveGoodsPicRequest> insertPicList = saveGoodsPicRequestList.stream().filter(e -> e.getId() == null || e.getId() == 0).collect(Collectors.toList());
        this.updateBatchById(PojoUtils.map(updatePicList, GoodsPicDO.class));
        picIds.removeAll(updatePicList.stream().map(e -> e.getId()).collect(Collectors.toList()));
        if(CollUtil.isNotEmpty(picIds)) {
            picIds.forEach(e->{
                GoodsPicDO goodsPicDO=new GoodsPicDO();
                goodsPicDO.setId(e);
                this.deleteByIdWithFill(goodsPicDO);
            });
        }

        List<GoodsPicDO> list=PojoUtils.map(insertPicList,GoodsPicDO.class);
        return this.saveBatch(list);
    }
}
