package com.wbu.train.business.daily_train.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train.mapper.DailyTrainMapper;
import com.wbu.train.business.daily_train.req.DailyTrainQueryReq;
import com.wbu.train.business.daily_train.req.DailyTrainSaveReq;
import com.wbu.train.business.daily_train.resp.DailyTrainQueryResp;
import com.wbu.train.business.daily_train.service.DailyTrainService;
import org.springframework.stereotype.Service;

/**
 * @author 钟正保
 * @description 针对表【dailyTrain(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain>
        implements DailyTrainService {

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
}




