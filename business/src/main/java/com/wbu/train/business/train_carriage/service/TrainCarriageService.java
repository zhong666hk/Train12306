package com.wbu.train.business.train_carriage.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.business.train_carriage.req.TrainCarriageQueryReq;
import com.wbu.train.business.train_carriage.req.TrainCarriageSaveReq;
import com.wbu.train.business.train_carriage.resp.TrainCarriageQueryResp;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【trainCarriage(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface TrainCarriageService extends IService<TrainCarriage> {
    public boolean saveTrainCarriage(TrainCarriageSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<TrainCarriageQueryResp> queryTrainCarriages(TrainCarriageQueryReq req);

    /**
     * 删除trainCarriage 通过id
     * @param id
     */
    public boolean  deleteById(Long id);

    /**
     * 查询当前车次的所有车厢数据
     */
    public List<TrainCarriage> selectByTrainCode(String trainCode);
}