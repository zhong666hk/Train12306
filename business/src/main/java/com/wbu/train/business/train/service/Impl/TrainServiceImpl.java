package com.wbu.train.business.train.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.train.domain.Train;
import com.wbu.train.business.train.mapper.TrainMapper;
import com.wbu.train.business.train.req.TrainQueryReq;
import com.wbu.train.business.train.req.TrainSaveReq;
import com.wbu.train.business.train.resp.TrainQueryResp;
import com.wbu.train.business.train.service.TrainService;
import com.wbu.train.common.util.SnowUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【train(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train>
        implements TrainService {

    @Override
    public boolean saveTrain(TrainSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Train train = BeanUtil.copyProperties(req, Train.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(train.getId())) {
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(date);
            train.setUpdateTime(date);
            return this.save(train);
        }// id不为空说明是修改的操作
        else {
            train.setUpdateTime(date);
            return this.updateById(train);
        }
    }

    @Override
    public Page<TrainQueryResp> queryTrains(TrainQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Train> trainQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<Train> page = this.page(new Page<>(req.getPage(), req.getSize()), trainQueryWrapper);
        Page<TrainQueryResp> trainQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, trainQueryRespPage);
        return trainQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public List<TrainQueryResp> queryAll() {
        QueryWrapper<Train> trainQueryWrapper = new QueryWrapper<>();
        trainQueryWrapper.orderByAsc("code");
        List<Train> list = this.list(trainQueryWrapper);
        return BeanUtil.copyToList(list, TrainQueryResp.class);
    }
}




