package com.wbu.train.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbu.train.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 钟正保
* @description 针对表【member(会员)】的数据库操作Mapper
* @createDate 2023-11-09 15:57:21
* @Entity generator.domain.Member
*/
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}




