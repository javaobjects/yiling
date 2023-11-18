package com.yiling.b2b.app.cart.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

/**
 * 获取商品基本信息
 * @author: zhigang.guo
 * @date: 2021/09/13
 */
@UtilityClass
public class SimpleGoodInfoUtils {


    private FileService fileService;

    // 获取beanUtil
    static {

        fileService = SpringUtils.getBean(FileService.class);
    }

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

        return   agreementGoodsList.stream().map(e -> {

            return SimpleGoodInfoUtils.toSimpleGoodsVO(e);

        }).collect(Collectors.toList());
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
        simpleGoodsVO.setPictureUrl(SimpleGoodInfoUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
        simpleGoodsVO.setSpecification(standardGoodsBasicDTO.getSellSpecifications());
        simpleGoodsVO.setUnit(standardGoodsBasicDTO.getSellUnit());
        simpleGoodsVO.setName(standardGoodsBasicDTO.getStandardGoods().getName());
        simpleGoodsVO.setManufacturer(standardGoodsBasicDTO.getStandardGoods().getManufacturer());
        simpleGoodsVO.setLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
        return simpleGoodsVO;
    }

    /**
     * 获取商品地址
     * @param pic
     * @return
     */
    public String getGoodsPicUrl(String pic) {

        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        }

        return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
    }

}
