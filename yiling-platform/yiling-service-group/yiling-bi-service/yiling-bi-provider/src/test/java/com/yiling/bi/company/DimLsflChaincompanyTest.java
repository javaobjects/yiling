package com.yiling.bi.company;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.company.api.DimLsflChaincompanyApi;
import com.yiling.bi.company.dto.DimLsflChaincompanyDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/2/14
 */
@Slf4j
public class DimLsflChaincompanyTest extends BaseTest {

    @Autowired
    private DimLsflChaincompanyApi dimLsflChaincompanyApi;

    @Test
    public void getByDbCodeAndChainCodeTest() {
        List<DimLsflChaincompanyDTO> list = dimLsflChaincompanyApi.getByDbCodeAndChainCode("24416", "yl005");
        System.out.println(list.size());
    }
}
