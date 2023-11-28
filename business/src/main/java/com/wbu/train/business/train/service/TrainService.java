package com.wbu.train.business.train.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.train.domain.Train;
import com.wbu.train.business.train.req.TrainQueryReq;
import com.wbu.train.business.train.req.TrainSaveReq;
import com.wbu.train.business.train.resp.TrainQueryResp;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【train(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface TrainService extends IService<Train> {
    public boolean saveTrain(TrainSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<TrainQueryResp> queryTrains(TrainQueryReq req);

    /**
     * 删除train 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}