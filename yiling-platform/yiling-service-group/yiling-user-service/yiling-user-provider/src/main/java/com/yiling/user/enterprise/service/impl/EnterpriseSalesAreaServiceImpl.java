package com.yiling.user.enterprise.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.dao.EnterpriseSalesAreaMapper;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDO;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaService;
import com.yiling.user.shop.dto.AreaChildrenDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业销售区域 服务实现类
 * </p>
 *
 * @author zhouxuan
 * @date 2021-10-29
 */
@Slf4j
@Service
public class EnterpriseSalesAreaServiceImpl extends BaseServiceImpl<EnterpriseSalesAreaMapper, EnterpriseSalesAreaDO> implements EnterpriseSalesAreaService {

    @Override
    public EnterpriseSalesAreaDO getByEid(Long eid) {
        QueryWrapper<EnterpriseSalesAreaDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseSalesAreaDO::getEid, eid).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EnterpriseSalesAreaDO> listByEids(List<Long> eids) {
        QueryWrapper<EnterpriseSalesAreaDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseSalesAreaDO::getEid, eids);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean save(Long eid, String areaJsonString ,Long opUserId) {
        if(Objects.isNull(eid) || StrUtil.isEmpty(areaJsonString)){
            return true;
        }
        //获取销售区域Json
        EnterpriseSalesAreaDO salesAreaDO = this.getByEid(eid);
        //获取描述
        String description = getDescription(areaJsonString, 3);

        if (Objects.isNull(salesAreaDO)) {
            salesAreaDO = new EnterpriseSalesAreaDO();
            salesAreaDO.setEid(eid);
            salesAreaDO.setJsonContent(areaJsonString);
            salesAreaDO.setDescription(description);
            salesAreaDO.setOpUserId(opUserId);
            return this.save(salesAreaDO);

        } else {
            salesAreaDO.setJsonContent(areaJsonString);
            salesAreaDO.setDescription(description);
            salesAreaDO.setOpUserId(opUserId);
            salesAreaDO.setUpdateTime(new Date());
            return this.updateById(salesAreaDO);

        }

    }

    public String getDescription(String areaJsonString ,Integer level){
        //解析json得出区域描述
        StringBuilder sb = new StringBuilder();
        List<AreaChildrenDTO> provinceList = JSONObject.parseArray(areaJsonString, AreaChildrenDTO.class);
        if (CollUtil.isEmpty(provinceList)) {
            log.info("请求区域json数据异常，异常数据：{}", JSONObject.toJSONString(areaJsonString));
            return null;
        }

        sb.append(provinceList.size());
        sb.append("个省");

        if (level > 1) {
            //城市数量
            Integer citySize = provinceList.stream().map(areaChildrenDTO -> {
                if(CollUtil.isNotEmpty(areaChildrenDTO.getChildren())){
                    return areaChildrenDTO.getChildren().size();
                }
                return 0;
            }).reduce(0, Integer::sum);

            sb.append(citySize);
            sb.append("个市");
        }
        if (level > 2) {
            //区县数量
            Integer sumAreaSize = provinceList.stream().map(areaChildrenDTO -> areaChildrenDTO.getChildren().stream().map(cityDto -> {
                if (CollUtil.isNotEmpty(cityDto.getChildren())) {
                    System.out.println(JSONObject.toJSONString(cityDto.getChildren()));
                    return cityDto.getChildren().size();
                }
                return 0;
            }).reduce(0, Integer::sum)).reduce(0, Integer::sum);

            sb.append(sumAreaSize);
            sb.append("个区域");
        }

        return sb.toString();
    }

}
