package com.wbu.train.business.daily_train_seat.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.daily_train_seat.domain.DailyTrainSeat;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatQueryReq;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatSaveReq;
import com.wbu.train.business.daily_train_seat.resp.DailyTrainSeatQueryResp;

import java.util.Date;
import java.util.List;

/**
* @author 钟正保
* @description 针对表【dailyTrainSeat(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface DailyTrainSeatService extends IService<DailyTrainSeat> {
    public boolean saveDailyTrainSeat(DailyTrainSeatSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<DailyTrainSeatQueryResp> queryDailyTrainSeats(DailyTrainSeatQueryReq req);

    /**
     * 删除dailyTrainSeat 通过id
     * @param id
     */
    public boolean  deleteById(Long id);

    void genDaily(Date date, String trainCode);

    /**
     * 计算当前类型的票数
     * @param date 时间
     * @param trainCode 火车编号
     * @param seatType 座位类型
     */
    public int countSeat(Date date, String trainCode, String seatType);

    List<DailyTrainSeat> selectByCarriage(Date date, String trainCode, Integer carriageIndex);
}