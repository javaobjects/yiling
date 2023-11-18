package com.yiling.mall.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.mall.recommend.bo.RecommendGoodsBO;
import com.yiling.mall.recommend.dao.RecommendGoodsMapper;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendGoodsPageListRequest;
import com.yiling.mall.recommend.entity.RecommendDO;
import com.yiling.mall.recommend.entity.RecommendGoodsDO;
import com.yiling.mall.recommend.service.RecommendGoodsService;
import com.yiling.mall.recommend.service.RecommendService;

import cn.hutool.core.lang.Assert;

/**
 * <p>
 * 推荐商品表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Service
public class RecommendGoodsServiceImpl extends BaseServiceImpl<RecommendGoodsMapper, RecommendGoodsDO> implements RecommendGoodsService {

    @Autowired
    private FileService fileService;

    @Autowired
    private RecommendService recommendService;

    @Override
    public Page<RecommendGoodsDO> pageList(QueryRecommendGoodsPageListRequest request) {
        Assert.notNull(request.getRecommendId(), "查询推荐位商品明细：recommendId为空！");
        return this.baseMapper.selectPage(request.getPage(), new LambdaQueryWrapper<RecommendGoodsDO>()
                .eq(RecommendGoodsDO::getRecommendId, request.getRecommendId()).orderByDesc(RecommendGoodsDO::getSort));
    }

    @Override
    public List<RecommendGoodsBO> queryRecommendGoodsList(Integer num) {
        // 推荐位后台默认仅有一条。
        RecommendDO recommendDO = recommendService.getOne(new LambdaQueryWrapper<RecommendDO>()
                .eq(RecommendDO::getStatus, EnableStatusEnum.ENABLED.getCode())
                .last("limit 1"));
        if (recommendDO == null || recommendDO.getId() == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RecommendGoodsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendGoodsDO::getRecommendId, recommendDO.getId())
                .orderByDesc(RecommendGoodsDO::getSort);
        if (num > 0) {
            queryWrapper.last("limit " + num);
        }
        List<RecommendGoodsBO> bos = PojoUtils.map(this.baseMapper.selectList(queryWrapper), RecommendGoodsBO.class);
        bos.forEach(e -> {
            // 处理图片
            e.setPic(this.getBannerPicUrl(e.getPic()));
        });
        return bos;
    }

    /**
     * 获取智能推荐获取商品信息
     *
     * @param recommendId
     * @return
     */
    @Override
    public List<RecommendGoodsDTO> getRecommendGoodsBytRecommendId(Long recommendId) {
        QueryWrapper<RecommendGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RecommendGoodsDO :: getRecommendId,recommendId);
        return PojoUtils.map(list(wrapper),RecommendGoodsDTO.class);
    }

    /**
     * 获取图片值
     * @param pic
     * @return
     */
    private String getBannerPicUrl(String pic) {
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
        }
    }
}
