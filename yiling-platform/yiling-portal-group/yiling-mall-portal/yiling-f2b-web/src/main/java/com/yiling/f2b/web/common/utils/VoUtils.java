package com.yiling.f2b.web.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.web.goods.vo.GoodsDetailVO;
import com.yiling.f2b.web.goods.vo.StandardGoodsPicVO;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/6/15
 */
@Component
public class VoUtils {

    @Autowired
    PictureUrlUtils pictureUrlUtils;
    /**
     * 协议商品对象转成前端商品对象
     * @param agreementGoodsDTO
     * @return
     */
    public SimpleGoodsVO toSimpleGoodsVO(AgreementGoodsDTO agreementGoodsDTO) {
        if (agreementGoodsDTO == null) {
            return null;
        }

        SimpleGoodsVO simpleGoodsVO = PojoUtils.map(agreementGoodsDTO, SimpleGoodsVO.class);
        simpleGoodsVO.setId(agreementGoodsDTO.getGoodsId());
        simpleGoodsVO.setName(agreementGoodsDTO.getGoodsName());
        simpleGoodsVO.setSpecification(agreementGoodsDTO.getSellSpecifications());
        return simpleGoodsVO;
    }

    /**
     * 协议商品对象集合转前端商品对象集合
     * @param agreementGoodsList
     * @return
     */
    public List<SimpleGoodsVO> toSimpleGoodsListByAgreementGoods(List<AgreementGoodsDTO> agreementGoodsList) {
        if (CollUtil.isEmpty(agreementGoodsList)) {
            return Collections.emptyList();
        }

        List<SimpleGoodsVO> simpleGoodsVOList = new ArrayList<>(agreementGoodsList.size());
        agreementGoodsList.forEach(e -> simpleGoodsVOList.add(this.toSimpleGoodsVO(e)));

        return simpleGoodsVOList;
    }

    /**
     * 标准库商品信息转前端商品信息对象
     * @param standardGoodsBasicDTO
     * @return
     */
    public SimpleGoodsVO toSimpleGoodsVO(StandardGoodsBasicDTO standardGoodsBasicDTO) {
        if (standardGoodsBasicDTO == null) {
            return null;
        }

        SimpleGoodsVO simpleGoodsVO = PojoUtils.map(standardGoodsBasicDTO, SimpleGoodsVO.class);
        simpleGoodsVO.setPictureUrl(pictureUrlUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
        simpleGoodsVO.setSpecification(standardGoodsBasicDTO.getSellSpecifications());
        simpleGoodsVO.setUnit(standardGoodsBasicDTO.getSellUnit());
        simpleGoodsVO.setName(standardGoodsBasicDTO.getStandardGoods().getName());
        simpleGoodsVO.setManufacturer(standardGoodsBasicDTO.getStandardGoods().getManufacturer());
        simpleGoodsVO.setLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
        return simpleGoodsVO;
    }

    /**
     * 后端商品所有详情转前端商品详情
     * @param goodsFullDTO
     * @return
     */
    public GoodsDetailVO toGoodsDetailVO(GoodsFullDTO goodsFullDTO) {
        GoodsDetailVO goodsDetailVO = PojoUtils.map(goodsFullDTO.getStandardGoodsAllInfo(), GoodsDetailVO.class);
        SimpleGoodsVO goodsInfo = new SimpleGoodsVO();
        goodsInfo.setId(goodsFullDTO.getId());
        goodsInfo.setName(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getName());
        goodsInfo.setGoodsType(goodsFullDTO.getGoodsType());
        goodsInfo.setLicenseNo(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getLicenseNo());
        goodsInfo.setManufacturer(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getManufacturer());
        goodsInfo.setSpecification(goodsFullDTO.getSellSpecifications());
        goodsInfo.setUnit(goodsFullDTO.getSellUnit());
        goodsInfo.setOverSoldType(goodsFullDTO.getOverSoldType());
        goodsInfo.setPictureUrl(pictureUrlUtils.getGoodsPicUrl(goodsFullDTO.getPic()));

        goodsDetailVO.setGoodsInfo(goodsInfo);

        //如果商品图片为空需要赋值默认图片
        if (CollUtil.isEmpty(goodsDetailVO.getPicBasicsInfoList())) {
            StandardGoodsPicVO standardGoodsPicVO = new StandardGoodsPicVO();
            standardGoodsPicVO.setPic(goodsFullDTO.getPic());
            standardGoodsPicVO.setPicDefault(1);
            standardGoodsPicVO.setPicOrder(0);
            List<StandardGoodsPicVO> picBasicsInfoList = new ArrayList<>();
            picBasicsInfoList.add(standardGoodsPicVO);
            picBasicsInfoList.forEach(e->{
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
            goodsDetailVO.setPicBasicsInfoList(picBasicsInfoList);
        }else{
            goodsDetailVO.getPicBasicsInfoList().forEach(e->{
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
        }
        return goodsDetailVO;
    }

}
