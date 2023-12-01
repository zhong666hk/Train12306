package com.wbu.train.business.train_seat.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.train_seat.domain.TrainSeat;
import com.wbu.train.business.train_seat.mapper.TrainSeatMapper;
import com.wbu.train.business.train_seat.req.TrainSeatQueryReq;
import com.wbu.train.business.train_seat.req.TrainSeatSaveReq;
import com.wbu.train.business.train_seat.resp.TrainSeatQueryResp;
import com.wbu.train.business.train_seat.service.TrainSeatService;
import org.springframework.stereotype.Service;

/**
 * @author 钟正保
 * @description 针对表【trainSeat(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat>
        implements TrainSeatService {

    @Override
    public boolean saveTrainSeat(TrainSeatSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(trainSeat.getId())){
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(date);
            trainSeat.setUpdateTime(date);
            return this.save(trainSeat);
        }// id不为空说明是修改的操作
        else {
            trainSeat.setUpdateTime(date);
            return this.updateById(trainSeat);
        }
    }

    @Override
    public Page<TrainSeatQueryResp> queryTrainSeats(TrainSeatQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<TrainSeat> trainSeatQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            trainSeatQueryWrapper.eq("train_code",req.getTrainCode());
        }
        trainSeatQueryWrapper.orderByAsc("train_code","carriage_index");

        Page<TrainSeat> page = this.page(new Page<>(req.getPage(), req.getSize()), trainSeatQueryWrapper);
        Page<TrainSeatQueryResp> trainSeatQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,trainSeatQueryRespPage);
        return trainSeatQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




