package com.wbu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.req.MemberTicketSaveReq;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.member.domain.Ticket;
import com.wbu.train.member.mapper.TicketMapper;
import com.wbu.train.member.req.TicketQueryReq;
import com.wbu.train.member.resp.TicketQueryResp;
import com.wbu.train.member.service.TicketService;
import org.springframework.stereotype.Service;

/**
 * @author 钟正保
 * @description 针对表ticket的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket>
        implements TicketService {

    @Override
    public boolean saveTicket(MemberTicketSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        // 添加的操作
        ticket.setId(SnowUtil.getSnowflakeNextId());
        ticket.setCreateTime(date);
        ticket.setUpdateTime(date);
        return this.save(ticket);

    }

    @Override
    public Page<TicketQueryResp> queryTickets(TicketQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Ticket> ticketQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Long memberId = LoginMemberContext.getId();
        ticketQueryWrapper.eq("member_id",memberId);
        if (ObjectUtil.isNotNull(req.getDate())){
            ticketQueryWrapper.eq("date",req.getDate());
        }
        Page<Ticket> page = this.page(new Page<>(req.getPage(), req.getSize()), ticketQueryWrapper);
        Page<TicketQueryResp> ticketQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, ticketQueryRespPage);
        return ticketQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }
}




