package com.wbu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(passenger.getId())){
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(date);
            passenger.setUpdateTime(date);
            passenger.setMemberId(LoginMemberContext.getId());
            return this.save(passenger);
        }// id不为空说明是修改的操作
        else {
            passenger.setUpdateTime(date);
            return this.updateById(passenger);
        }
    }

    @Override
    public Page<PassengerQueryResp> queryPassengers(PassengerQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Passenger> passengerQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(req) && ObjectUtil.isNotEmpty(req.getMemberId())) {
            passengerQueryWrapper.eq("member_id", req.getMemberId());
        }
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<Passenger> page = this.page(new Page<>(req.getPage(), req.getSize()), passengerQueryWrapper);
        Page<PassengerQueryResp> passengerQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,passengerQueryRespPage);
        return passengerQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




