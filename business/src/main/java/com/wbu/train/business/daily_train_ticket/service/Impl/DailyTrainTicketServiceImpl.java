package com.wbu.train.business.daily_train_ticket.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import com.wbu.train.business.daily_train_seat.service.Impl.DailyTrainSeatServiceImpl;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.mapper.DailyTrainTicketMapper;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketQueryReq;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketSaveReq;
import com.wbu.train.business.daily_train_ticket.resp.DailyTrainTicketQueryResp;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import com.wbu.train.business.train_station.domain.TrainStation;
import com.wbu.train.business.train_station.service.TrainStationService;
import com.wbu.train.common.enums.SeatTypeEnum;
import com.wbu.train.common.enums.TrainTypeEnum;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 钟正保
 * @description 针对表【dailyTrainTicket(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket>
        implements DailyTrainTicketService {
    private Logger LOG = LoggerFactory.getLogger(DailyTrainSeatServiceImpl.class);
    @Autowired
    private TrainStationService trainStationService;
    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @Override
    public boolean saveDailyTrainTicket(DailyTrainTicketSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
            dailyTrainTicketQueryWrapper.eq("date", req.getDate())
                    .eq("train_code", req.getTrainCode())
                    .eq("start", req.getStart())
                    .eq("end", req.getEnd());
            List<DailyTrainTicket> list = this.list(dailyTrainTicketQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                throw new MyException(40000, "已存在该火车车票,");
            }
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(date);
            dailyTrainTicket.setUpdateTime(date);
            return this.save(dailyTrainTicket);
        }// id不为空说明是修改的操作
        else {
            dailyTrainTicket.setUpdateTime(date);
            return this.updateById(dailyTrainTicket);
        }
    }

    @Override
    public Page<DailyTrainTicketQueryResp> queryDailyTrainTickets(DailyTrainTicketQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            dailyTrainTicketQueryWrapper.eq("train_code", req.getTrainCode());
        }
        if (ObjectUtil.isNotEmpty(req.getStart())) {
            dailyTrainTicketQueryWrapper.eq("start", req.getStart());
        }
        if (ObjectUtil.isNotEmpty(req.getEnd())) {
            dailyTrainTicketQueryWrapper.eq("end", req.getEnd());
        }
        if (ObjectUtil.isNotNull(req.getDate())) {
            dailyTrainTicketQueryWrapper.eq("date", req.getDate());
        }
        dailyTrainTicketQueryWrapper.orderByDesc("date");
        dailyTrainTicketQueryWrapper.orderByAsc("start_index");
        Page<DailyTrainTicket> page = this.page(new Page<>(req.getPage(), req.getSize()), dailyTrainTicketQueryWrapper);
        Page<DailyTrainTicketQueryResp> dailyTrainTicketQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page, dailyTrainTicketQueryRespPage);
        return dailyTrainTicketQueryRespPage;
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
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode) {
        LOG.info("开始生成{}天  {}车次的余票信息", DateUtil.format(date, "yyyy-MM-dd"), trainCode);
        //1.先删除
        QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
        dailyTrainTicketQueryWrapper.eq("`date`", date).eq("train_code", trainCode);
        this.remove(dailyTrainTicketQueryWrapper);
        //2.再生成

        // 2.1查询途径的车站信息
        List<TrainStation> stationList = trainStationService.getTrainStationByTrainCode(trainCode);
        if (ObjectUtil.isEmpty(stationList)) {
            throw new MyException(40000, "余票生成失败，请先完善" + trainCode + "车次的车站信息");
        }
        //2.2循环生成车票信息 ABCDE  4 3 2 1 A_>B A_>C.... D->E
        DateTime now = DateTime.now();
        ArrayList<DailyTrainTicket> dailyTrainTicketArrayList = new ArrayList<>();
        // 单项的嵌套循环
        if (stationList.size()<2){
            throw new MyException(40000,"余票信息生成失败,该车次车站数少于2信息");
        }
        for (int i = 0; i < stationList.size(); i++) {
            // 得到出发站
            TrainStation startStation = stationList.get(i);
            BigDecimal sumKM = BigDecimal.ZERO;
            for (int j = i+1; j < stationList.size(); j++) {
                // 终点站
                TrainStation endStation = stationList.get(j);
                // 统计公里数
                sumKM= sumKM.add(endStation.getKm());
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(startStation.getName());
                dailyTrainTicket.setStartPinyin(startStation.getNamePinyin());
                dailyTrainTicket.setStartTime(startStation.getOutTime());
                dailyTrainTicket.setStartIndex(startStation.getIndex());
                dailyTrainTicket.setEnd(endStation.getName());
                dailyTrainTicket.setEndPinyin(endStation.getNamePinyin());
                dailyTrainTicket.setEndTime(endStation.getInTime());
                dailyTrainTicket.setEndIndex(endStation.getIndex());
                //座位类型的票数
                int ydz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getKey());
                int edz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getKey());
                int yw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getKey());
                int rw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getKey());
                //票价=里程总数*座位单价*车次类型系数
                String trainType = dailyTrain.getType();
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                BigDecimal ydzPrice = sumKM.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKM.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKM.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwzPrice = sumKM.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);

                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwzPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketArrayList.add(dailyTrainTicket);
            }
        }
        if (!this.saveBatch(dailyTrainTicketArrayList,20)){
            throw new MyException(40000,"余票信息生成失败");
        }
        LOG.info("生成{}天  {}车次的余票信息完成", DateUtil.formatDate(date), trainCode);
    }

    /**
     * 根据唯一键来查询余票
     * @param date 时间
     * @param trainCode 火车
     * @param start 起点站
     * @param end 终点
     */
    @Override
    public DailyTrainTicket selectByUniqueKey(Date date,String trainCode,String start,String end){
        QueryWrapper<DailyTrainTicket> dailyTrainTicketQueryWrapper = new QueryWrapper<>();
        dailyTrainTicketQueryWrapper
                .eq("date",date)
                .eq("train_code",trainCode)
                .eq("start",start)
                .eq("end",end);
        List<DailyTrainTicket> dailyTrainTicketList = this.list(dailyTrainTicketQueryWrapper);
        if (CollectionUtil.isNotEmpty(dailyTrainTicketList)){
            return dailyTrainTicketList.get(0);
        }else {
            return null;
        }
    }


    @Override
    public Integer updateCountBySell(Date date, String trainCode, String seatTypeCode, Integer minStartIndex, Integer maxStartIndex, Integer minEndIndex, Integer maxEndIndex) {
        UpdateWrapper<DailyTrainTicket> dailyTrainTicketUpdateWrapper = new UpdateWrapper<>();
        dailyTrainTicketUpdateWrapper
                .setSql(seatTypeCode.equals("1"),"ydz = ydz - 1")
                .setSql(seatTypeCode.equals("2"),"edz= edz -1")
                .setSql(seatTypeCode.equals("3"),"rw= rw -1")
                .setSql(seatTypeCode.equals("4"),"yw= yw -1")
                .eq("date", date)
                .eq("train_code", trainCode)
                .ge("start_index", minStartIndex)
                .le("start_index", maxStartIndex)
                .ge("end_index", minEndIndex)
                .le("end_index", maxEndIndex);

        return baseMapper.update(null, dailyTrainTicketUpdateWrapper);
    }

}




