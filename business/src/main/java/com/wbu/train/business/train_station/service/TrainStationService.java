package com.wbu.train.business.train_station.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.business.train_station.req.TrainStationQueryReq;
import com.wbu.train.business.train_station.req.TrainStationSaveReq;
import com.wbu.train.business.train_station.resp.TrainStationQueryResp;

/**
* @author 钟正保
* @description 针对表【train_station(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface TrainStationService extends IService<TrainStation> {
    public boolean saveTrainStation(TrainStationSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<TrainStationQueryResp> queryTrainStations(TrainStationQueryReq req);

    /**
     * 删除train_station 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}