package com.wbu.train.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbu.train.member.service.CodeInformationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CodeInformationServiceImplTest {
    @Autowired
    private CodeInformationService codeInformationService;

    @Test
    public void testService(){
        long count = codeInformationService.count();
        System.out.println(count);
    }
}