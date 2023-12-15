package com.wbu.train.business.daily_train_carriage.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.daily_train_carriage.domain.DailyTrainCarriage;
import com.wbu.train.business.daily_train_carriage.mapper.DailyTrainCarriageMapper;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageQueryReq;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageSaveReq;
import com.wbu.train.business.daily_train_carriage.resp.DailyTrainCarriageQueryResp;
import com.wbu.train.business.daily_train_carriage.service.DailyTrainCarriageService;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.business.train_carriage.service.TrainCarriageService;
import com.wbu.train.common.enums.SeatColEnum;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【daily_train_carriage(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage>
        implements DailyTrainCarriageService {
    private Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageServiceImpl.class);

    @Autowired
    private TrainCarriageService trainCarriageService;

    @Override
    public boolean saveDailyTrainCarriage(DailyTrainCarriageSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 计算座位数和列数
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(colsByType.size());
        req.setSeatCount(colsByType.size() * req.getRowCount());

        // 拷贝类
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            QueryWrapper<DailyTrainCarriage> dailyTrainCarriageQueryWrapper = new QueryWrapper<>();
            dailyTrainCarriageQueryWrapper.eq("train_code", req.getTrainCode())
                    .eq("`index`", req.getIndex());
            List<DailyTrainCarriage> trainCarriageList = this.list(dailyTrainCarriageQueryWrapper);

            if (CollectionUtil.isNotEmpty(trainCarriageList)) {
                throw new MyException(AppExceptionExample.DAILY_TRAIN_CARRIAGE_HAS_EXIST);
            }
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(date);
            dailyTrainCarriage.setUpdateTime(date);
            return this.save(dailyTrainCarriage);
        }// id不为空说明是修改的操作
        else {
            dailyTrainCarriage.setUpdateTime(date);
            return this.updateById(dailyTrainCarriage);
        }
    }

    @Override
    public Page<DailyTrainCarriageQueryResp> queryDailyTrainCarriages(DailyTrainCarriageQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrainCarriage> dailyTrainCarriageQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            dailyTrainCarriageQueryWrapper.eq("train_code", req.getTrainCode());
        }
        if (ObjectUtil.isNotNull(req.getDate())) {
            dailyTrainCarriageQueryWrapper.eq("date", req.getDate());
        }
        dailyTrainCarriageQueryWrapper.orderByDesc("date");
        dailyTrainCarriageQueryWrapper.orderByAsc("train_code", "`index`");

        Page<DailyTrainCarriage> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainCarriageQueryWrapper);
        Page<DailyTrainCarriageQueryResp> dailyTrainCarriageQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, dailyTrainCarriageQueryRespPage);
        return dailyTrainCarriageQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }


    @Override
    @Transactional
    public void genDaily(Date date, String trainCode) {
        LOG.info("开始生成{}天  {}车次的车站信息", DateUtil.format(date, "yyyy-MM-dd"), trainCode);
        //1.先删除
        QueryWrapper<DailyTrainCarriage> dailyTrainCarriageQueryWrapper = new QueryWrapper<>();
        dailyTrainCarriageQueryWrapper.eq("`date`", date).eq("train_code", trainCode);
        this.remove(dailyTrainCarriageQueryWrapper);
        //2.再生成
        //2.1 先查该车次的所有的车厢的基础信息
        List<TrainCarriage> carriageList = trainCarriageService.getTrainCarriageByTrainCarriageCode(trainCode);
        if (CollectionUtil.isEmpty(carriageList)) {
            throw new MyException(40000, "当前车次还未添加车厢信息");
        }
        //2.2循环carriage来生成每日车厢信息
        for (TrainCarriage carriage : carriageList) {
            DateTime now = DateTime.now();
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(carriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setDate(date);
            if (!this.save(dailyTrainCarriage)) {
                throw new MyException(40000, "生成每日车厢异常");
            }
        }
        LOG.info("生成{}天  {}车次的车厢信息完成", DateUtil.formatDate(date), trainCode);
    }

    /**
     * 根据SeatType来查询车厢
     * @return
     */
   @Override
    public List<DailyTrainCarriage> selectBySeatType(Date date,String trainCode,String seatType){
        QueryWrapper<DailyTrainCarriage> dailyTrainCarriageQueryWrapper = new QueryWrapper<>();
        dailyTrainCarriageQueryWrapper
                .eq("date",date)
                .eq("train_code",trainCode)
                .eq("seat_type",seatType);
        return this.list(dailyTrainCarriageQueryWrapper);
    }
}




