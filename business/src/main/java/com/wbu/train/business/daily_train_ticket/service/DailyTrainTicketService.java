package com.wbu.train.business.daily_train_ticket.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketQueryReq;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketSaveReq;
import com.wbu.train.business.daily_train_ticket.resp.DailyTrainTicketQueryResp;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author 钟正保
* @description 针对表【dailyTrainTicket(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface DailyTrainTicketService extends IService<DailyTrainTicket> {
    public boolean saveDailyTrainTicket(DailyTrainTicketSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<DailyTrainTicketQueryResp> queryDailyTrainTickets(DailyTrainTicketQueryReq req);

    /**
     * 删除dailyTrainTicket 通过id
     * @param id
     */
    public boolean  deleteById(Long id);


    @Transactional
    void genDaily(DailyTrain dailyTrain, Date date, String trainCode);

    DailyTrainTicket selectByUniqueKey(Date date, String trainCode, String start, String end);
}