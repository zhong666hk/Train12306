package com.wbu.train.business.daily_train_station.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.daily_train_station.domain.DailyTrainStation;
import com.wbu.train.business.daily_train_station.mapper.DailyTrainStationMapper;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationQueryReq;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationSaveReq;
import com.wbu.train.business.daily_train_station.resp.DailyTrainStationQueryResp;
import com.wbu.train.business.daily_train_station.service.DailyTrainStationService;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.business.train_station.service.TrainStationService;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【dailyTrainStation(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainStationServiceImpl extends ServiceImpl<DailyTrainStationMapper, DailyTrainStation>
        implements DailyTrainStationService {

    @Autowired
    private TrainStationService trainStationService;
    private Logger LOG = LoggerFactory.getLogger(DailyTrainStationServiceImpl.class);
    @Override
    public boolean saveDailyTrainStation(DailyTrainStationSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            QueryWrapper<DailyTrainStation> dailyTrainStationQueryWrapper = new QueryWrapper<>();
            dailyTrainStationQueryWrapper.eq("train_code",req.getTrainCode())
                    .eq("`index`",req.getIndex())
                    .or((queryWrapper)->{
                        queryWrapper.eq("train_code",req.getTrainCode())
                                .eq("name",req.getName());
                    });
            List<DailyTrainStation> list = this.list(dailyTrainStationQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)){
                throw new MyException(AppExceptionExample.DAILY_TRAIN_STATION_HAS_EXIST);
            }
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(date);
            dailyTrainStation.setUpdateTime(date);
            return this.save(dailyTrainStation);
        }// id不为空说明是修改的操作
        else {
            dailyTrainStation.setUpdateTime(date);
            return this.updateById(dailyTrainStation);
        }
    }

    @Override
    public Page<DailyTrainStationQueryResp> queryDailyTrainStations(DailyTrainStationQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrainStation> dailyTrainStationQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            dailyTrainStationQueryWrapper.eq("train_code",req.getTrainCode());
        }
        if (ObjectUtil.isNotNull(req.getDate())){
            dailyTrainStationQueryWrapper.eq("`date`",req.getDate());
        }
        dailyTrainStationQueryWrapper.orderByDesc("`date`");
        dailyTrainStationQueryWrapper.orderByAsc("train_code","`index`");

        Page<DailyTrainStation> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainStationQueryWrapper);
        Page<DailyTrainStationQueryResp> dailyTrainStationQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, dailyTrainStationQueryRespPage);
        return dailyTrainStationQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public void genDaily(Date date, String trainCode) {
        LOG.info("开始生成{}天  {}车次的车站信息",DateUtil.format(date,"yyyy-MM-dd"),trainCode);
        //1.先删除
        QueryWrapper<DailyTrainStation> dailyTrainStationQueryWrapper = new QueryWrapper<>();
        dailyTrainStationQueryWrapper.eq("`date`",date).eq("train_code",trainCode);
        this.remove(dailyTrainStationQueryWrapper);
        //2.再生成
            //2.1 先查该车次的所有的车站的基础信息
        List<TrainStation> stationList = trainStationService.getTrainStationByTrainCode(trainCode);
        if (CollectionUtil.isEmpty(stationList)){
            throw new MyException(40000,"当前车次还未添加车站信息");
        }
            //2.2循环station来生成每日车站信息
        for (TrainStation trainStation : stationList) {
            DateTime now = DateTime.now();
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setDate(date);
            if (!this.save(dailyTrainStation)) {
                throw new MyException(40000,"生成每日车站异常");
            }
        }
        LOG.info("生成{}天  {}车站的车站信息完成",DateUtil.formatDate(date),trainCode);
    }
}




