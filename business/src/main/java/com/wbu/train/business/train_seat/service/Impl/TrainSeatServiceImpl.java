package com.wbu.train.business.train_seat.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.business.train_carriage.service.TrainCarriageService;
import com.wbu.train.common.enums.SeatColEnum;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.train_seat.domain.TrainSeat;
import com.wbu.train.business.train_seat.mapper.TrainSeatMapper;
import com.wbu.train.business.train_seat.req.TrainSeatQueryReq;
import com.wbu.train.business.train_seat.req.TrainSeatSaveReq;
import com.wbu.train.business.train_seat.resp.TrainSeatQueryResp;
import com.wbu.train.business.train_seat.service.TrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【trainSeat(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat>
        implements TrainSeatService {
    @Autowired
    private TrainCarriageService trainCarriageService;

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

    @Override
    @Transactional
    public boolean genTrainSeat(String trainCode) {
        // 1. 防止一辆火车重复生成 --->先删除再生成 / 先查看数据库是否存在在生成
        QueryWrapper<TrainSeat> trainSeatQueryWrapper = new QueryWrapper<>();
        trainSeatQueryWrapper.eq("train_code",trainCode);
        this.remove(trainSeatQueryWrapper);
        // 定义时间
        DateTime dateTime = DateUtil.dateSecond();
        //定义皮插入的列表
        ArrayList<TrainSeat> trainSeatArrayList = new ArrayList<>();
        // 查询当前车次所有的车厢,根据车厢类型-->1等座 4列 2等座 5列
        List<TrainCarriage> trainCarriageList = trainCarriageService.selectByTrainCode(trainCode);
        if (CollectionUtil.isEmpty(trainCarriageList)){
            return false;
        }
        // 循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : trainCarriageList) {
            // 拿到车厢的行数，座位类型
            Integer row = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            //每个车厢的座位索引
            int seatIndex=1;
            // 根据车厢的座位类型，选出每一列座位编号 AA BB CC ....
            List<SeatColEnum> colsByType = SeatColEnum.getColsByType(seatType);
            //循环行数
            for (int i = 1; i <= row; i++) {
                // 循环列数
                for (SeatColEnum seatColEnum : colsByType) {
                    // 构造数据填充到数据库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(i),'0',2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(dateTime);
                    trainSeat.setUpdateTime(dateTime);
                    trainSeatArrayList.add(trainSeat);
                }
            }

        }
        return this.saveBatch(trainSeatArrayList);
    }
}




