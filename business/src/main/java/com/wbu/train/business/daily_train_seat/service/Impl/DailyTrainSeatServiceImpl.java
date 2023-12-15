package com.wbu.train.business.daily_train_seat.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train_seat.domain.TrainSeat;
import com.wbu.train.business.train_seat.service.TrainSeatService;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.business.train_station.service.TrainStationService;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train_seat.domain.DailyTrainSeat;
import com.wbu.train.business.daily_train_seat.mapper.DailyTrainSeatMapper;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatQueryReq;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatSaveReq;
import com.wbu.train.business.daily_train_seat.resp.DailyTrainSeatQueryResp;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【dailyTrainSeat(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat>
        implements DailyTrainSeatService {
    @Autowired
    private TrainSeatService trainSeatService;
    @Autowired
    private TrainStationService trainStationService;
    private Logger LOG = LoggerFactory.getLogger(DailyTrainSeatServiceImpl.class);
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
        dailyTrainSeatQueryWrapper.orderByAsc("train_code","carriage_index","carriage_seat_index");

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

    @Override
    @Transactional
    public void genDaily(Date date, String trainCode) {
        LOG.info("开始生成{}天  {}车次的座位信息", DateUtil.format(date, "yyyy-MM-dd"), trainCode);
        //1.先删除
        QueryWrapper<DailyTrainSeat> dailyTrainSeatQueryWrapper = new QueryWrapper<>();
        dailyTrainSeatQueryWrapper.eq("`date`", date).eq("train_code", trainCode);
        this.remove(dailyTrainSeatQueryWrapper);
        //2.再生成
        //2.1 先查该车次的所有的座位的基础信息
        List<TrainSeat> seatList = trainSeatService.getTrainSeatByTrainCode(trainCode);
        if (CollectionUtil.isEmpty(seatList)) {
            throw new MyException(40000, "当前车次还未添加座位信息");
        }
        // 2.2 获取有多少个站,来设置站到站的座位是否sell
        List<TrainStation> stationList = trainStationService.getTrainStationByTrainCode(trainCode);
        String sell= StrUtil.fillBefore("",'0',stationList.size()-1);
        //2.3循环seat来生成每日座位信息
        for (TrainSeat seat : seatList) {
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(seat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(sell);
            if (!this.save(dailyTrainSeat)) {
                throw new MyException(40000, "生成每日座位异常");
            }
        }
        LOG.info("生成{}天  {}车次的座位信息完成", DateUtil.formatDate(date), trainCode);
    }

    /**
     * 计算当前类型的票数
     * @param date 时间
     * @param trainCode 火车编号
     * @param seatType 座位类型
     */
    @Override
    public int countSeat(Date date, String trainCode, String seatType){
        QueryWrapper<DailyTrainSeat> dailyTrainSeatQueryWrapper = new QueryWrapper<>();
        dailyTrainSeatQueryWrapper.eq("date",date)
                .eq("train_code",trainCode)
                .eq("seat_type",seatType);
        long count = this.count(dailyTrainSeatQueryWrapper);
        if (count==0L){
            return -1;
        }
        return (int) count;
    }

    /**
     * 根据车厢序号查询座位
     * @param date 时间
     * @param trainCode 车次
     * @param carriageIndex 车厢序号
     */
    @Override
    public List<DailyTrainSeat> selectByCarriage(Date date,String trainCode ,Integer carriageIndex){
        QueryWrapper<DailyTrainSeat> dailyTrainSeatQueryWrapper = new QueryWrapper<>();
        dailyTrainSeatQueryWrapper
                .eq("date",date)
                .eq("train_code",trainCode)
                .eq("carriage_index",carriageIndex)
                .orderByAsc("carriage_seat_index");
        return this.list(dailyTrainSeatQueryWrapper);
    }
}




