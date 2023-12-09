package com.wbu.train.business.daily_train_ticket.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.mapper.DailyTrainTicketMapper;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketQueryReq;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketSaveReq;
import com.wbu.train.business.daily_train_ticket.resp.DailyTrainTicketQueryResp;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【dailyTrainTicket(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket>
        implements DailyTrainTicketService {

    @Override
    public boolean saveDailyTrainTicket(DailyTrainTicketSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainTicket.getId())){
            QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
            dailyTrainTicketQueryWrapper.eq("date",req.getDate())
                    .eq("train_code",req.getTrainCode())
                    .eq("start",req.getStart())
                    .eq("end",req.getEnd());
            List<DailyTrainTicket> list = this.list(dailyTrainTicketQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)){
                throw new MyException(40000,"已存在该火车车票,");
            }
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(date);
            dailyTrainTicket.setUpdateTime(date);
            return this.save(dailyTrainTicket);
        }// id不为空说明是修改的操作
        else {
            dailyTrainTicket.setUpdateTime(date);
            return this.updateById(dailyTrainTicket);
        }
    }

    @Override
    public Page<DailyTrainTicketQueryResp> queryDailyTrainTickets(DailyTrainTicketQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            dailyTrainTicketQueryWrapper.eq("train_code",req.getTrainCode());
        }
        if (ObjectUtil.isNotNull(req.getDate())){
            dailyTrainTicketQueryWrapper.eq("date",req.getDate());
        }
        dailyTrainTicketQueryWrapper.orderByDesc("date");
        dailyTrainTicketQueryWrapper.orderByAsc("start_index");
        Page<DailyTrainTicket> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainTicketQueryWrapper);
        Page<DailyTrainTicketQueryResp> dailyTrainTicketQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,dailyTrainTicketQueryRespPage);
        return dailyTrainTicketQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




