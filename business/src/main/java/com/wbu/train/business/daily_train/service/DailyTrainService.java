package com.wbu.train.business.daily_train.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train.req.DailyTrainQueryReq;
import com.wbu.train.business.daily_train.req.DailyTrainSaveReq;
import com.wbu.train.business.daily_train.resp.DailyTrainQueryResp;

/**
* @author 钟正保
* @description 针对表【dailyTrain(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface DailyTrainService extends IService<DailyTrain> {
    public boolean saveDailyTrain(DailyTrainSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<DailyTrainQueryResp> queryDailyTrains(DailyTrainQueryReq req);

    /**
     * 删除dailyTrain 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}