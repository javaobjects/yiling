package com.yiling.basic.location.service.impl;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.basic.location.enums.IPLocationDataSourceEnum;
import com.yiling.basic.location.service.IPLocationQueryService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ip2region 数据库方式获取
 *
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Slf4j
@Service("ip2RegionIPLocationQueryService")
@RefreshScope
public class IP2RegionIPLocationQueryServiceImpl implements IPLocationQueryService {

    @Value("${ip.location.verify.ip}")
    private String ip;
    @Value("${ip.location.verify.result.ip2region}")
    private String result;
    @Value("${ip.location.query.ip2region.dbFilePath}")
    private String dbFilePath;
    private byte[] vIndex;

    @PostConstruct
    public void init() {
        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        try {
            vIndex = Searcher.loadVectorIndexFromFile(dbFilePath);
        } catch (Exception e) {
            log.error("failed to load vector index from `{}`: {}", dbFilePath, e);
            return;
        }
    }

    @Override
    public IPLocationBO query(String ip) {
        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithVectorIndex(dbFilePath, vIndex);
        } catch (Exception e) {
            log.error("failed to create vectorIndex cached searcher with `{}`: {}", dbFilePath, e);
            return null;
        }

        // 3、查询
        try {
            String region = searcher.search(ip);
            if (StrUtil.isNotEmpty(region)) {
                String[] array = region.split("\\|");

                IPLocationBO ipLocationBO = new IPLocationBO(ip, array[0], array[2], array[3], "", array[4], IPLocationDataSourceEnum.IP2REGION);
                return ipLocationBO;
            }
        } catch (Exception e) {
            log.error("failed to search ({})", ip, e);
        }

        // 4、关闭资源
        try {
            searcher.close();
        } catch (IOException e) {
            log.error("failed to close searcher", e);
        }

        return null;
    }

    @Override
    public Boolean verify() {
        IPLocationBO ipLocationBO = this.query(ip);
        return ipLocationBO.toString().equals(result);
    }

}
