package com.wbu.train.business.train_carriage.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.common.enums.SeatColEnum;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.business.train_carriage.mapper.TrainCarriageMapper;
import com.wbu.train.business.train_carriage.req.TrainCarriageQueryReq;
import com.wbu.train.business.train_carriage.req.TrainCarriageSaveReq;
import com.wbu.train.business.train_carriage.resp.TrainCarriageQueryResp;
import com.wbu.train.business.train_carriage.service.TrainCarriageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【trainCarriage(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TrainCarriageServiceImpl extends ServiceImpl<TrainCarriageMapper, TrainCarriage>
        implements TrainCarriageService {
    private Logger LOG = LoggerFactory.getLogger(TrainCarriageServiceImpl.class);
    @Override
    public boolean saveTrainCarriage(TrainCarriageSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 计算座位数和列数
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(colsByType.size());
        req.setSeatCount(colsByType.size()*req.getRowCount());

        // 拷贝类
        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(trainCarriage.getId())){
            QueryWrapper<TrainCarriage> trainCarriageQueryWrapper = new QueryWrapper<>();
            trainCarriageQueryWrapper.eq("train_code",req.getTrainCode())
                    .eq("`index`",req.getIndex());
            List<TrainCarriage> trainCarriageList = this.list(trainCarriageQueryWrapper);
            if (CollectionUtil.isNotEmpty(trainCarriageList)){
                throw new MyException(AppExceptionExample.TRAIN_CARRIAGE_HAS_EXIST);
            }
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(date);
            trainCarriage.setUpdateTime(date);
            return this.save(trainCarriage);
        }// id不为空说明是修改的操作
        else {
            trainCarriage.setUpdateTime(date);
            return this.updateById(trainCarriage);
        }
    }

    @Override
    public Page<TrainCarriageQueryResp> queryTrainCarriages(TrainCarriageQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<TrainCarriage> trainCarriageQueryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(req.getTrainCode())){
            trainCarriageQueryWrapper.eq("train_code",req.getTrainCode());
        }
        trainCarriageQueryWrapper.orderByAsc("train_code","`index`");
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<TrainCarriage> page = this.page(new Page<>(req.getPage(), req.getSize()), trainCarriageQueryWrapper);
        Page<TrainCarriageQueryResp> trainCarriageQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,trainCarriageQueryRespPage);
        return trainCarriageQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        QueryWrapper<TrainCarriage> trainCarriageQueryWrapper = new QueryWrapper<>();
        trainCarriageQueryWrapper.eq("train_code",trainCode)
                .orderByAsc("`index`");
        return this.list(trainCarriageQueryWrapper);
    }

    @Override
    public List<TrainCarriage> getTrainCarriageByTrainCarriageCode(String trainCode) {
        LOG.info("获取站台基本信息getTrainCarriageByTrainCode trainCode={}",trainCode);
        QueryWrapper<TrainCarriage> trainCarriageQueryWrapper = new QueryWrapper<>();
        trainCarriageQueryWrapper.eq("train_code",trainCode).
                orderByAsc("`index`");
        LOG.info("获取站台基本信息getTrainCarriageByTrainCode完成");
        return this.list(trainCarriageQueryWrapper);
    }
}




