package com.wbu.train.business.train_station.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.business.train_station.mapper.TrainStationMapper;
import com.wbu.train.business.train_station.req.TrainStationQueryReq;
import com.wbu.train.business.train_station.req.TrainStationSaveReq;
import com.wbu.train.business.train_station.resp.TrainStationQueryResp;
import com.wbu.train.business.train_station.service.TrainStationService;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【train_station(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TrainStationServiceImpl extends ServiceImpl<TrainStationMapper, TrainStation>
        implements TrainStationService {
    private Logger LOG = LoggerFactory.getLogger(TrainStationServiceImpl.class);
    @Override
    public boolean saveTrainStation(TrainStationSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(trainStation.getId())) {
            QueryWrapper<TrainStation> trainStationQueryWrapper = new QueryWrapper<>();
            trainStationQueryWrapper.eq("train_code",req.getTrainCode())
                    .eq("`index`",req.getIndex())
                    .or((queryWrapper)->{
                        queryWrapper.eq("train_code",req.getTrainCode())
                                .eq("name",req.getName());
                    });
            List<TrainStation> list = this.list(trainStationQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)){
                throw new MyException(AppExceptionExample.TRAIN_STATION_HAS_EXIST);
            }

            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(date);
            trainStation.setUpdateTime(date);
            return this.save(trainStation);
        }// id不为空说明是修改的操作
        else {
            trainStation.setUpdateTime(date);
            return this.updateById(trainStation);
        }
    }

    @Override
    public Page<TrainStationQueryResp> queryTrainStations(TrainStationQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<TrainStation> trainStationQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            trainStationQueryWrapper.eq("train_code",req.getTrainCode());
        }

        trainStationQueryWrapper.orderByAsc("train_code","`index`");
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<TrainStation> page = this.page(new Page<>(req.getPage(), req.getSize()), trainStationQueryWrapper);
        Page<TrainStationQueryResp> trainStationQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, trainStationQueryRespPage);
        return trainStationQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public List<TrainStation> getTrainStationByTrainCode(String trainCode) {
        LOG.info("获取站台基本信息getTrainStationByTrainCode trainCode={}",trainCode);
        QueryWrapper<TrainStation> trainStationQueryWrapper = new QueryWrapper<>();
        trainStationQueryWrapper.eq("train_code",trainCode).
                orderByAsc("`index`");
        LOG.info("获取站台基本信息getTrainStationByTrainCode完成");
        return this.list(trainStationQueryWrapper);
    }
}




