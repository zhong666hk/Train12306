package com.wbu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.member.domain.Passenger;
import com.wbu.train.member.mapper.PassengerMapper;
import com.wbu.train.member.req.PassengerQueryReq;
import com.wbu.train.member.req.PassengerSaveReq;
import com.wbu.train.member.resp.PassengerQueryResp;
import com.wbu.train.member.service.PassengerService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<PassengerQueryResp> queryPassengers(PassengerQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Passenger> passengerQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(req) && ObjectUtil.isNotNull(req.getMemberId())){
            passengerQueryWrapper.eq("member_id",req.getMemberId());
        }
        List<Passenger> passengerList = this.list(passengerQueryWrapper);
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }
}




