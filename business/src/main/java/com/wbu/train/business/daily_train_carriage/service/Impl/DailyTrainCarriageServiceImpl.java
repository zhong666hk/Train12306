package com.wbu.train.business.daily_train_carriage.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.common.enums.SeatColEnum;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train_carriage.domain.DailyTrainCarriage;
import com.wbu.train.business.daily_train_carriage.mapper.DailyTrainCarriageMapper;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageQueryReq;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageSaveReq;
import com.wbu.train.business.daily_train_carriage.resp.DailyTrainCarriageQueryResp;
import com.wbu.train.business.daily_train_carriage.service.DailyTrainCarriageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【daily_train_carriage(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage>
        implements DailyTrainCarriageService {

    @Override
    public boolean saveDailyTrainCarriage(DailyTrainCarriageSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 计算座位数和列数
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(colsByType.size());
        req.setSeatCount(colsByType.size()*req.getRowCount());

        // 拷贝类
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainCarriage.getId())){
            QueryWrapper<DailyTrainCarriage> dailyTrainCarriageQueryWrapper = new QueryWrapper<>();
            dailyTrainCarriageQueryWrapper.eq("train_code",req.getTrainCode())
                    .eq("`index`",req.getIndex());
            List<DailyTrainCarriage> trainCarriageList = this.list(dailyTrainCarriageQueryWrapper);

            if (CollectionUtil.isNotEmpty(trainCarriageList)){
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
        if(ObjectUtil.isNotEmpty(req.getTrainCode())){
            dailyTrainCarriageQueryWrapper.eq("train_code",req.getTrainCode());
        }
        if(ObjectUtil.isNotNull(req.getDate())){
            dailyTrainCarriageQueryWrapper.eq("date",req.getDate());
        }
        dailyTrainCarriageQueryWrapper.orderByDesc("date");
        dailyTrainCarriageQueryWrapper.orderByAsc("train_code","`index`");

        Page<DailyTrainCarriage> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainCarriageQueryWrapper);
        Page<DailyTrainCarriageQueryResp> dailyTrainCarriageQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,dailyTrainCarriageQueryRespPage);
        return dailyTrainCarriageQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




