package com.yiling.goods.standard.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.BaseTest;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;

/**
 * @author shichen
 * @类名 StandardGoodsSpecificationTest
 * @描述
 * @创建时间 2022/8/26
 * @修改人 shichen
 * @修改时间 2022/8/26
 **/
public class StandardGoodsSpecificationTest extends BaseTest {
    @Autowired
    private StandardGoodsSpecificationService standardGoodsSpecificationService;


    @Test
    public void test1(){
        StandardSpecificationPageRequest request = new StandardSpecificationPageRequest();
        request.setYlFlag(1);
        Page<StandardSpecificationGoodsInfoBO> page = standardGoodsSpecificationService.getSpecificationGoodsInfoPage(request);
        System.out.println(page);
    }
}
