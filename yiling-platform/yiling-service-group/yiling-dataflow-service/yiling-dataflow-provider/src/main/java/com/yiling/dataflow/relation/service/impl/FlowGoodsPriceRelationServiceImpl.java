package com.yiling.dataflow.relation.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.relation.dao.FlowGoodsPriceRelationMapper;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsPriceRelationRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsPriceRelationDO;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
@Service
@CacheConfig(cacheNames = "dataflow:flowGoodsPriceRelation")
public class FlowGoodsPriceRelationServiceImpl extends BaseServiceImpl<FlowGoodsPriceRelationMapper, FlowGoodsPriceRelationDO> implements FlowGoodsPriceRelationService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 当月无销售的企业，Redis缓存key
     */
    public static final String ERP_FLOW_SALE_SIGN_GOODS_CRM_CODE = "erp_flow_sale_sign_goods_crm_code";

    @Override
    @Cacheable(key = "#goodsName+'+'+#spec+'+getByGoodsNameAndSpec'")
    public FlowGoodsPriceRelationDO getByGoodsNameAndSpec(String goodsName, String spec) {
        QueryWrapper<FlowGoodsPriceRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsPriceRelationDO::getOldGoodsName, goodsName);
        queryWrapper.lambda().eq(FlowGoodsPriceRelationDO::getSpec, spec);
        queryWrapper.last("limit 1");

        return this.getOne(queryWrapper);
    }

    @Override
    public FlowGoodsPriceRelationDTO getByGoodsNameAndSpecAndEnterpriseCode(String goodsName, String spec, String enterpriseCode) {
        if (StrUtil.isEmpty(goodsName) || StrUtil.isEmpty(spec) || StrUtil.isEmpty(enterpriseCode)) {
            return null;
        }

        FlowGoodsPriceRelationDTO flowGoodsPriceRelationDTO = null;
        Object jsonObject = stringRedisTemplate.opsForHash().get(ERP_FLOW_SALE_SIGN_GOODS_CRM_CODE, goodsName + "-" + spec + "-" + enterpriseCode);
        if (jsonObject != null) {
            flowGoodsPriceRelationDTO = JSON.parseObject(String.valueOf(jsonObject), FlowGoodsPriceRelationDTO.class);
            return flowGoodsPriceRelationDTO;
        } else {
            QueryWrapper<FlowGoodsPriceRelationDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FlowGoodsPriceRelationDO::getOldGoodsName, goodsName);
            queryWrapper.lambda().eq(FlowGoodsPriceRelationDO::getSpec, spec);
            queryWrapper.lambda().eq(FlowGoodsPriceRelationDO::getCustomerCode, enterpriseCode);
            queryWrapper.last("limit 1");

            flowGoodsPriceRelationDTO = PojoUtils.map(this.getOne(queryWrapper), FlowGoodsPriceRelationDTO.class);

            if (flowGoodsPriceRelationDTO != null) {
                stringRedisTemplate.opsForHash().put(ERP_FLOW_SALE_SIGN_GOODS_CRM_CODE, goodsName + "-" + spec + "-" + enterpriseCode, JSON.toJSONString(flowGoodsPriceRelationDTO));
                return flowGoodsPriceRelationDTO;
            }
        }
        return null;
    }

    @Override
    public boolean save(SaveFlowGoodsPriceRelationRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getOldGoodsName(), "参数 oldGoodsName 不能为空");
        Assert.notNull(request.getSpec(), "参数 spec 不能为空");
        Assert.notNull(request.getCustomerCode(), "参数 customerCode 不能为空");
        FlowGoodsPriceRelationDTO priceRelationDTO = this.getByGoodsNameAndSpecAndEnterpriseCode(request.getOldGoodsName(), request.getSpec(), request.getCustomerCode());
        if(null!=priceRelationDTO){
            return false;
        }
        return this.save(PojoUtils.map(request, FlowGoodsPriceRelationDO.class));
    }
}
