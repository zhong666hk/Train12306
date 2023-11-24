package com.wbu.train.business.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.station.domain.Station;
import com.wbu.train.business.station.mapper.StationMapper;
import com.wbu.train.business.station.req.StationQueryReq;
import com.wbu.train.business.station.req.StationSaveReq;
import com.wbu.train.business.station.resp.StationQueryResp;
import com.wbu.train.business.station.service.StationService;
import com.wbu.train.common.util.SnowUtil;
import org.springframework.stereotype.Service;

/**
 * @author 钟正保
 * @description 针对表【station(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station>
        implements StationService {

    @Override
    public boolean saveStation(StationSaveReq req) {
        DateTime date = DateUtil.date(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Station station = BeanUtil.copyProperties(req, Station.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(station.getId())) {
            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(date);
            station.setUpdateTime(date);
            return this.save(station);
        }// id不为空说明是修改的操作
        else {
            station.setUpdateTime(date);
            return this.updateById(station);
        }
    }

    @Override
    public Page<StationQueryResp> queryStations(StationQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Station> stationQueryWrapper = new QueryWrapper<>();

        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<Station> page = this.page(new Page<>(req.getPage(), req.getSize()), stationQueryWrapper);
        Page<StationQueryResp> stationQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, stationQueryRespPage);
        return stationQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return false;
        }
        return this.removeById(id);
    }
}




