package com.wbu.train.member.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.common.req.MemberTicketSaveReq;
import com.wbu.train.member.domain.Ticket;
import com.wbu.train.member.req.TicketQueryReq;
import com.wbu.train.member.resp.TicketQueryResp;

/**
 * @author 钟正保
 * @description 针对表ticket的数据库操作Service
 * @createDate 2023-11-14 14:43:47
 */
public interface TicketService extends IService<Ticket> {
    public boolean saveTicket(MemberTicketSaveReq req);

    /**
     * 查询当前登录用户的购票
     *
     * @param req
     * @return
     */
    public Page<TicketQueryResp> queryTickets(TicketQueryReq req);

    /**
     * 删除ticket 通过id
     *
     * @param id
     */
    public boolean deleteById(Long id);
}