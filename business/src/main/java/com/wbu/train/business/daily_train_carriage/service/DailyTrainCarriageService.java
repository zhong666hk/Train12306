package com.wbu.train.business.daily_train_carriage.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.daily_train_carriage.domain.DailyTrainCarriage;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageQueryReq;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageSaveReq;
import com.wbu.train.business.daily_train_carriage.resp.DailyTrainCarriageQueryResp;

/**
* @author 钟正保
* @description 针对表【daily_train_carriage(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface DailyTrainCarriageService extends IService<DailyTrainCarriage> {
    public boolean saveDailyTrainCarriage(DailyTrainCarriageSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<DailyTrainCarriageQueryResp> queryDailyTrainCarriages(DailyTrainCarriageQueryReq req);

    /**
     * 删除daily_train_carriage 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}