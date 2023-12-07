package com.wbu.train.business.daily_train_station.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.daily_train_station.domain.DailyTrainStation;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationQueryReq;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationSaveReq;
import com.wbu.train.business.daily_train_station.resp.DailyTrainStationQueryResp;

import java.util.Date;

/**
 * @author 钟正保
 * @description 针对表【DailyTrainStation(乘车人)】的数据库操作Service
 * @createDate 2023-11-14 14:43:47
 */
public interface DailyTrainStationService extends IService<DailyTrainStation> {
    public boolean saveDailyTrainStation(DailyTrainStationSaveReq req);

    /**
     * 查询当前登录用户的购票
     *
     * @param req
     * @return
     */
    public Page<DailyTrainStationQueryResp> queryDailyTrainStations(DailyTrainStationQueryReq req);

    /**
     * 删除DailyTrainStation 通过id
     *
     * @param id
     */
    public boolean deleteById(Long id);

    public void genDaily(Date date,String trainCode);
}