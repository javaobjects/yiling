package com.yiling.common.web.goods.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;

/**
 * @author: shuang.zhang
 * @date: 2021/6/23
 */
@Component
public class PictureUrlUtils {

    @Autowired
    private FileService fileService;

    public String getGoodsPicUrl(String pic) {
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
        }
    }
}
