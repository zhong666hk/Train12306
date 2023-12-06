package com.wbu.train.business.daily_train_seat.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train_seat.domain.DailyTrainSeat;
import com.wbu.train.business.daily_train_seat.mapper.DailyTrainSeatMapper;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatQueryReq;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatSaveReq;
import com.wbu.train.business.daily_train_seat.resp.DailyTrainSeatQueryResp;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import org.springframework.stereotype.Service;

/**
 * @author 钟正保
 * @description 针对表【dailyTrainSeat(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat>
        implements DailyTrainSeatService {

    @Override
    public boolean saveDailyTrainSeat(DailyTrainSeatSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainSeat.getId())){
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(date);
            dailyTrainSeat.setUpdateTime(date);
            return this.save(dailyTrainSeat);
        }// id不为空说明是修改的操作
        else {
            dailyTrainSeat.setUpdateTime(date);
            return this.updateById(dailyTrainSeat);
        }
    }

    @Override
    public Page<DailyTrainSeatQueryResp> queryDailyTrainSeats(DailyTrainSeatQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrainSeat> dailyTrainSeatQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            dailyTrainSeatQueryWrapper.eq("train_code",req.getTrainCode());
        }
        if (ObjectUtil.isNotNull(req.getDate())){
            dailyTrainSeatQueryWrapper.eq("date",req.getDate());
        }
        dailyTrainSeatQueryWrapper.orderByDesc("date");
        dailyTrainSeatQueryWrapper.orderByAsc("train_code","carriage_index");

        Page<DailyTrainSeat> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainSeatQueryWrapper);
        Page<DailyTrainSeatQueryResp> dailyTrainSeatQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,dailyTrainSeatQueryRespPage);
        return dailyTrainSeatQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




