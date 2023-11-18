package com.yiling.mall.banner.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.mall.banner.dao.BannerMapper;
import com.yiling.mall.banner.dto.request.CheckBannerRequest;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.dto.request.SaveBannerRequest;
import com.yiling.mall.banner.dto.request.UpdateBannerRequest;
import com.yiling.mall.banner.entity.BannerDO;
import com.yiling.mall.banner.entity.BannerGoodsDO;
import com.yiling.mall.banner.enums.BannerLinkTypeEnum;
import com.yiling.mall.banner.service.BannerGoodsService;
import com.yiling.mall.banner.service.BannerService;
import com.yiling.mall.common.request.GoodsRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;

/**
 * <p>
 * banner表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BannerServiceImpl extends BaseServiceImpl<BannerMapper, BannerDO> implements BannerService {

    @Autowired
    private BannerGoodsService bannerGoodsService;

    @Autowired
    private FileService fileService;

    @Override
    public Page<BannerDO> pageList(QueryBannerPageListRequest request) {
        if(request.getEndTime() != null){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        if(request.getStartTime() != null){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }

        if(request.getCreateTimeEnd() != null){
            request.setCreateTimeEnd(DateUtil.endOfDay(request.getCreateTimeEnd()));
        }
        if(request.getCreateTimeStart() != null){
            request.setCreateTimeStart(DateUtil.beginOfDay(request.getCreateTimeStart()));
        }
        Page<BannerDO> page = this.baseMapper.pageList(request.getPage(), request);
        page.getRecords().forEach(e -> {
            // 处理图片
            e.setPic(this.getBannerPicUrl(e.getPic()));
        });
        return page;
    }

    @Override
    public BannerDO get(Long bannerId) {
        Assert.notNull(bannerId, "获取banner明细：bannerId为空！");
        BannerDO bannerDO = this.baseMapper.selectById(bannerId);
        return bannerDO;
    }

    @Override
    public Boolean addBanner(SaveBannerRequest request) {
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        bannerDO.setStatus(EnableStatusEnum.ENABLED.getCode());
        this.checkoutRequest(bannerDO);
        this.baseMapper.insert(bannerDO);
        Assert.notNull(bannerDO.getId(), "保存Banner异常！");
        // 保存商品列表信息
        if (BannerLinkTypeEnum.GOODS.getCode().equals(request.getLinkType() ) && CollUtil.isNotEmpty(request.getGoodsList())) {
            bannerGoodsService.saveBatch(this.doBannerGoodsDOList(bannerDO.getId(), request.getGoodsList()));
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateBanner(UpdateBannerRequest request) {
        if(request.getStartTime() != null){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime() != null){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        Assert.notNull(bannerDO.getId(), "编辑保存banner：banneId为空！");
        this.checkoutRequest(bannerDO);
        this.baseMapper.updateById(bannerDO);
        // 保存商品列表信息
        if ( BannerLinkTypeEnum.GOODS.getCode().equals(request.getLinkType()) && CollUtil.isNotEmpty(request.getGoodsList())) {
            // 先删除，再保存
            bannerGoodsService.remove(new LambdaQueryWrapper<BannerGoodsDO>().eq(BannerGoodsDO::getBannerId, bannerDO.getId()));
            bannerGoodsService.saveBatch(this.doBannerGoodsDOList(bannerDO.getId(), request.getGoodsList()));
        }
        return Boolean.TRUE;
    }

    @Override
    public List<BannerDO> queryAvailableBannerList(Integer num) {
        QueryBannerPageListRequest request = new QueryBannerPageListRequest();
        request.setStatus(EnableStatusEnum.ENABLED.getCode());
        // 展示所有已开始未结束的banner(开始时间和结束时间）
        Date availableTime = new Date();
        request.setAvailableTime(availableTime);
        if (num > 0) {
            request.setSize(num);
        }else {
            request.setSize(Integer.MAX_VALUE);
        }
        return this.pageList(request).getRecords();
    }

    @Override
    public Boolean checkRightful(CheckBannerRequest request) {
        Assert.notBlank(request.getTitle(), "校验标题合法：标题不能为空!");
        Boolean titleRightful = this.baseMapper.selectCount(new LambdaQueryWrapper<BannerDO>().eq(BannerDO::getTitle, request.getTitle())) == 0;
        Assert.isTrue(titleRightful, "校验标题合法：标题已存在!");
        Assert.isTrue(request.getStartTime() != null && request.getEndTime() != null, "校验投放时间合法：投放时间不能为空!");
        Date now = new Date();
        Assert.isTrue(now.before(request.getStartTime()), "校验投放时间合法：投放时间应大于当前时间!");
        Assert.isTrue(request.getStartTime().before(request.getEndTime()), "校验投放时间合法：投放开始时间应小于投放结束时间!");
        return Boolean.TRUE;
    }

    /**
     * 修改状态
     *
     * @param id     bannerId
     * @param status 启用停用状态
     * @return
     */
    @Override
    public Boolean updateStatusById(Long id, Integer status,Long opUserId) {
        BannerDO one = new BannerDO();
        one.setId(id);
        one.setStatus(status);
        one.setUpdateUser(opUserId);
        return updateById(one);
    }

    /**
     * 处理banner商品明细DO
     * @param bannerId
     * @param goodsRequestList
     * @return
     */
    private List<BannerGoodsDO> doBannerGoodsDOList(Long bannerId, List<GoodsRequest> goodsRequestList) {
        // 去重
        List<GoodsRequest> goodsList = goodsRequestList.stream().distinct().collect(Collectors.toList());
        List<BannerGoodsDO> bannerGoodsDOList = new ArrayList<>();
        for (GoodsRequest goods : goodsList) {
            BannerGoodsDO bannerGoodsDO = new BannerGoodsDO();
            bannerGoodsDO.setBannerId(bannerId);
            bannerGoodsDO.setGoodsId(goods.getGoodsId());
            bannerGoodsDO.setSort(goods.getSort());
            bannerGoodsDOList.add(bannerGoodsDO);
        }
        return bannerGoodsDOList;
    }

    /**
     * 校验操作bannerDO是否合法
     * @param bannerDO
     * @return
     */
    private BannerDO checkoutRequest(BannerDO bannerDO) {
        Assert.notBlank(bannerDO.getTitle(), "banner标题为空！");
        Assert.notNull(bannerDO.getLinkType(), "banner链接类型为空！");
        Assert.notNull(bannerDO.getStartTime(), "banner投放开始时间为空！");
        Assert.notNull(bannerDO.getEndTime(), "banner投放结束时间为空！");
        Assert.notBlank(bannerDO.getPic(), "banner图片值为空！");
        // 默认eid为以岭ID
        if (bannerDO.getEid() == null || bannerDO.getEid() == 0) {
            bannerDO.setEid(Constants.YILING_EID);
        }
        if (bannerDO.getSort() == null) {
            bannerDO.setSort(200);
        }
        return bannerDO;
    }

    /**
     * 获取图片值
     * @param pic
     * @return
     */
    private String getBannerPicUrl(String pic) {
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.BANNER_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.BANNER_PICTURE);
        }
    }
}
