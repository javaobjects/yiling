package com.yiling.goods.medicine.service;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.BaseTest;
import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/11/10
 */
public class GoodsMatchServiceTest extends BaseTest {

    @Autowired
    GoodsMatchService goodsMatchService;
    @Autowired
    StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Test
    public void test1() {
        List<StandardGoodsSpecificationDO> standardGoodsSpecificationDTOList =getStandardGoodsSpecificationDTOAll();
        List<MatchGoodsDTO> targets=new ArrayList<>();
        for(StandardGoodsSpecificationDO standardGoodsSpecificationDTO:standardGoodsSpecificationDTOList){
            MatchGoodsDTO matchGoodsDTO=new MatchGoodsDTO();
            matchGoodsDTO.setName(standardGoodsSpecificationDTO.getName());
            matchGoodsDTO.setSpecification(standardGoodsSpecificationDTO.getSellSpecifications());
//            matchGoodsDTO.setLicenseno(standardGoodsSpecificationDTO.getLicenseNo());
            matchGoodsDTO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
            matchGoodsDTO.setId(standardGoodsSpecificationDTO.getId());
            targets.add(matchGoodsDTO);
        }
        MatchGoodsDTO matchGoodsDTO=new MatchGoodsDTO();
        matchGoodsDTO.setName("连花清瘟胶囊");
//        matchGoodsDTO.setLicenseno("国药准字H20045479");
        matchGoodsDTO.setSpecification("0.35g*24粒*10小盒");
        matchGoodsDTO.setManufacturer("石家庄以岭药业股份有限公司");
        MatchedGoodsDTO matchedGoodsDTO=goodsMatchService.matchingGoodsWithSpec(matchGoodsDTO,targets);
        System.out.println(matchedGoodsDTO);
    }

    public List<StandardGoodsSpecificationDO> getStandardGoodsSpecificationDTOAll() {
        //需要循环调用
        List<StandardGoodsSpecificationDO> list = new ArrayList<>();
        StandardSpecificationPageRequest request = new StandardSpecificationPageRequest();
        Page<StandardGoodsSpecificationDO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = standardGoodsSpecificationService.getSpecificationPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //插入任务
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }
}
