package com.wbu.train.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wbu.train.member.domain.CodeInformation;
import com.wbu.train.member.mapper.CodeInformationMapper;
import com.wbu.train.member.service.CodeInformationService;
import org.springframework.stereotype.Service;

/**
* @author 钟正保
* @description 针对表【code_information(验证码)】的数据库操作Service实现
* @createDate 2023-11-11 16:05:35
*/
@Service
public class CodeInformationServiceImpl extends ServiceImpl<CodeInformationMapper, CodeInformation>
    implements CodeInformationService {

}




