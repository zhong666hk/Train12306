package com.wbu.train.business.station.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.station.domain.Station;
import com.wbu.train.business.station.req.StationQueryReq;
import com.wbu.train.business.station.req.StationSaveReq;
import com.wbu.train.business.station.resp.StationQueryResp;


/**
* @author 钟正保
* @description 针对表【station(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface StationService extends IService<Station> {
    public boolean saveStation(StationSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<StationQueryResp> queryStations(StationQueryReq req);

    /**
     * 删除station 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}