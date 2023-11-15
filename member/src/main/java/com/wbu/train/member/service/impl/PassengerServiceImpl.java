package com.wbu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.member.domain.Passenger;
import com.wbu.train.member.mapper.PassengerMapper;
import com.wbu.train.member.req.PassengerSaveReq;
import com.wbu.train.member.service.PassengerService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 钟正保
* @description 针对表【passenger(乘车人)】的数据库操作Service实现
* @createDate 2023-11-14 14:43:47
*/
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
    implements PassengerService {

    @Override
    public boolean savePassenger(PassengerSaveReq req) {
        if (ObjectUtil.isNull(req)){
            return false;
        }
        // 拷贝类
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(new Date());
        passenger.setUpdateTime(new Date());
        passenger.setMemberId(LoginMemberContext.getId());
        return this.save(passenger);
    }
}




