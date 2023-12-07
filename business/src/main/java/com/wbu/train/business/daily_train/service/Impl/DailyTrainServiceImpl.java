package com.wbu.train.business.daily_train.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.daily_train_carriage.service.DailyTrainCarriageService;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import com.wbu.train.business.daily_train_station.service.DailyTrainStationService;
import com.wbu.train.business.train.domain.Train;
import com.wbu.train.business.train.service.TrainService;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train.mapper.DailyTrainMapper;
import com.wbu.train.business.daily_train.req.DailyTrainQueryReq;
import com.wbu.train.business.daily_train.req.DailyTrainSaveReq;
import com.wbu.train.business.daily_train.resp.DailyTrainQueryResp;
import com.wbu.train.business.daily_train.service.DailyTrainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【dailyTrain(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain>
        implements DailyTrainService {
    @Resource
    private TrainService trainService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Override
    public boolean saveDailyTrain(DailyTrainSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrain.getId())){
            // 唯一键校验
            QueryWrapper<DailyTrain> dailyTrainQueryWrapper = new QueryWrapper<>();
            dailyTrainQueryWrapper.eq("code",req.getCode());
            dailyTrainQueryWrapper.eq("date",req.getDate());
            List<DailyTrain> list = this.list(dailyTrainQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)){
                throw new MyException(AppExceptionExample.DAILY_TRAIN_CODE_HAS_EXIST);
            }
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(date);
            dailyTrain.setUpdateTime(date);
            return this.save(dailyTrain);
        }// id不为空说明是修改的操作
        else {
            dailyTrain.setUpdateTime(date);
            return this.updateById(dailyTrain);
        }
    }

    @Override
    public Page<DailyTrainQueryResp> queryDailyTrains(DailyTrainQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrain> dailyTrainQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotNull(req.getDate())){
            dailyTrainQueryWrapper.eq("date",req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())){
            dailyTrainQueryWrapper.eq("code",req.getCode());
        }
        dailyTrainQueryWrapper.orderByDesc("date");
        dailyTrainQueryWrapper.orderByAsc("code");
        Page<DailyTrain> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainQueryWrapper);
        Page<DailyTrainQueryResp> dailyTrainQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,dailyTrainQueryRespPage);
        return dailyTrainQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public boolean genDaily(Date date) {
        //1 获取基本车次信息
        List<Train> trainList = trainService.list();
        if (CollectionUtil.isEmpty(trainList)){
            throw new MyException(40000,"车次基础数据为空,生成失败");
        }
        // 2自动生成防止多次生成 先删除数据再生成
        for (Train train : trainList) {
            //2.1生成车次 车站信息
            if (!genDailyTrain(date, train)){
                throw new MyException(40000,"生成异常");
            }
        }
        return true;
    }

    private boolean genDailyTrain(Date date, Train train) {
        // 1.删除已有的数据
        QueryWrapper<DailyTrain> dailyTrainQueryWrapper = new QueryWrapper<>();
        dailyTrainQueryWrapper.eq("date", date).eq("code", train.getCode());
        this.remove(dailyTrainQueryWrapper);
        //2.生成数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        boolean save = this.save(dailyTrain);
        // 3.生成车站
        dailyTrainStationService.genDaily(date,train.getCode());
        //4.生成车厢
        dailyTrainCarriageService.genDaily(date,train.getCode());
        //5.生成每日座位
        dailyTrainSeatService.genDaily(date,train.getCode());
        return save;
    }
}




