package com.wbu.train.member.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.member.domain.Passenger;
import com.wbu.train.member.req.PassengerSaveReq;

/**
* @author 钟正保
* @description 针对表【passenger(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface PassengerService extends IService<Passenger> {
    public boolean savePassenger(PassengerSaveReq req);
}
