package com.wbu.train.member.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.member.domain.Passenger;
import com.wbu.train.member.req.PassengerQueryReq;
import com.wbu.train.member.req.PassengerSaveReq;
import com.wbu.train.member.resp.PassengerQueryResp;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【passenger(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface PassengerService extends IService<Passenger> {
    public boolean savePassenger(PassengerSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<PassengerQueryResp> queryPassengers(PassengerQueryReq req);

    /**
     * 删除passenger 通过id
     * @param id
     */
    public boolean  deleteById(Long id);

    List<PassengerQueryResp> queryMine();
}
