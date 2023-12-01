package com.wbu.train.business.train_seat.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.train_seat.domain.TrainSeat;
import com.wbu.train.business.train_seat.req.TrainSeatQueryReq;
import com.wbu.train.business.train_seat.req.TrainSeatSaveReq;
import com.wbu.train.business.train_seat.resp.TrainSeatQueryResp;

/**
* @author 钟正保
* @description 针对表【trainSeat(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface TrainSeatService extends IService<TrainSeat> {
    public boolean saveTrainSeat(TrainSeatSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<TrainSeatQueryResp> queryTrainSeats(TrainSeatQueryReq req);

    /**
     * 删除trainSeat 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}