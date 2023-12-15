package com.wbu.train.business.confirm_order.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.confirm_order.req.ConfirmOrderDoReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderTicketReq;
import com.wbu.train.business.daily_train_carriage.domain.DailyTrainCarriage;
import com.wbu.train.business.daily_train_carriage.service.DailyTrainCarriageService;
import com.wbu.train.business.daily_train_seat.domain.DailyTrainSeat;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.enums.ConfirmOrderStatusEnum;
import com.wbu.train.common.enums.SeatColEnum;
import com.wbu.train.common.enums.SeatTypeEnum;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.confirm_order.domain.ConfirmOrder;
import com.wbu.train.business.confirm_order.mapper.ConfirmOrderMapper;
import com.wbu.train.business.confirm_order.req.ConfirmOrderQueryReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderSaveReq;
import com.wbu.train.business.confirm_order.resp.ConfirmOrderQueryResp;
import com.wbu.train.business.confirm_order.service.ConfirmOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【confirmOrder】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder>
        implements ConfirmOrderService {
    @Autowired
    private DailyTrainTicketService dailyTrainTicketService;
    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;
    Logger LOG = LoggerFactory.getLogger(ConfirmOrderServiceImpl.class);

    @Override
    public boolean saveConfirmOrder(ConfirmOrderSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(confirmOrder.getId())){
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(date);
            confirmOrder.setUpdateTime(date);
            return this.save(confirmOrder);
        }// id不为空说明是修改的操作
        else {
            confirmOrder.setUpdateTime(date);
            return this.updateById(confirmOrder);
        }
    }

    @Override
    public Page<ConfirmOrderQueryResp> queryConfirmOrders(ConfirmOrderQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<ConfirmOrder> confirmOrderQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        if (ObjectUtil.isNotNull(req.getDate())){
            confirmOrderQueryWrapper.eq("date",req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            confirmOrderQueryWrapper.eq("train_code",req.getTrainCode());
        }
        Page<ConfirmOrder> page = this.page(new Page<>(req.getPage(), req.getSize()), confirmOrderQueryWrapper);
        Page<ConfirmOrderQueryResp> confirmOrderQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,confirmOrderQueryRespPage);
        return confirmOrderQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public void doConfirm(ConfirmOrderDoReq req) {
        // 数据校验 ConfirmOrderDoReq
        /*一、业务校验
         1.车次是否存在2.是否有余票3.车次是否在有效期内 4.tickets条数>0
           （ 前端校验了，主要防止绕过前端直接发送）
         5.同乘客同车次是否已经买过
         */

        // 二、保存确认订单表，状态初始化
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(LoginMemberContext.getId());
        Date reqDate = req.getDate();
        confirmOrder.setDate(reqDate);
        String reqTrainCode = req.getTrainCode();
        confirmOrder.setTrainCode(reqTrainCode);
        String reqStart = req.getStart();
        confirmOrder.setStart(reqStart);
        String reqEnd = req.getEnd();
        confirmOrder.setEnd(reqEnd);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        if (!this.save(confirmOrder)) {
            throw new MyException(4000,"插入异常");
        }

        // 三、查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUniqueKey(reqDate, reqTrainCode, reqStart, reqEnd);
        // 四、预扣减余票数量，并判断余票是否足够
        for (ConfirmOrderTicketReq ticketReq : tickets) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getKey, seatTypeCode);
            // 库存拦截
            reduceTickets(dailyTrainTicket, seatTypeEnum);
        }
            /* 计算偏移值 ---只有在选座的时候才去计算
                C1,D2 偏移值为[0,5]
                A1,B1,C1偏移值为[0,1,2]
                都是相对第一个座位来确定偏移值的
             */
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            LOG.info("本次购票有选座");
            //查出本次选哪种座位类型，用于计算类型
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            LOG.info("本次选座的座位类型包含的列{}",colEnumList);
            /*referSeatList
                A1 C1 D1 F1
                A2 C2 D2 F2
             */
            ArrayList<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode()+i);
                }
            }
            LOG.info("参照座位{}",referSeatList);
            //获取绝对偏移值 列表 --相对于referSeatList座位的偏移值
            ArrayList<Integer> absoluteOffsetList = new ArrayList<>();
            //相对第一个座位的偏移值 列表 相对于购买的第一个座位的偏移值
            ArrayList<Integer> offsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                absoluteOffsetList.add(index);
            }
            LOG.info("计算得到所有座位的绝对偏移值{}",absoluteOffsetList);
            for (Integer index : absoluteOffsetList) {
                offsetList.add(index-absoluteOffsetList.get(0));
            }
            LOG.info("计算得到所有座位的相对第一个座位的偏移值{}",offsetList);
            getSeat(reqDate,reqTrainCode,ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0],//从A1 得到A
                    offsetList,
                    dailyTrainTicket.getStartIndex(),dailyTrainTicket.getEndIndex());

        }else {
            LOG.info("本次选座没有选票");
            for (ConfirmOrderTicketReq ticket : tickets) {
                getSeat(reqDate,reqTrainCode,ticket.getSeatTypeCode(),
                        null, null,
                        dailyTrainTicket.getStartIndex(),dailyTrainTicket.getEndIndex());
            }

        }
        /* 五、选座
            1.每个车厢的获取座位数据
            2.挑选符合条件的座位，如果这个车厢不满足，则进入下一个车厢。(多个选座应该在同一个车厢）
         */


        /*六、选中座位后事务处理
            1.座位表修改售卖情况sell;
            2.余票详细表的修改
            3.为会员增加购买记录
            4.更新确认订单成功
         */
    }

    /**
     * 获取座位 如果有选座就一次性选好，如果没有选座就一个一个的挑
     * @param date 日期
     * @param trainCode 车次
     * @param seatType 座位类型
     * @param column 第一个座位的列号
     * @param offsetList 相对偏移数组
     */
    private void getSeat(Date date, String trainCode, String seatType,String column,List<Integer> offsetList,Integer startIndex
            ,Integer endIndex){
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        LOG.info("共查出符合条件车厢个数{}",dailyTrainCarriageList.size());
        // 一个车厢一个车厢的获取座位
        for (DailyTrainCarriage dailyTrainCarriage : dailyTrainCarriageList) {
            LOG.info("开始从车厢{}，选座位",dailyTrainCarriage.getIndex());
            List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatService.selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            LOG.info("{}车厢的座位数{}",dailyTrainCarriage.getIndex(),dailyTrainSeatList.size());
            for (DailyTrainSeat dailyTrainSeat : dailyTrainSeatList) {
                boolean isChoose = callSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose){
                    LOG.info("选中的座位");
                    return;
                }else {
                }
            }


        }
    }

    /**
     * 计算 dailyTrainSeat区间是否可以卖
     * 例如：sell=10001 表示购买区间 1~4 则区间已售000
     * @param dailyTrainSeat
     * 选中要计算购票后的sell,比如原来的100001100101 本次区间是1~4
     * 方案:构造本次购票造成的售卖信息011100000000 和原来的按位或
     */
    private boolean callSell(DailyTrainSeat dailyTrainSeat,Integer startIndex
    ,Integer endIndex) {
        String sell = dailyTrainSeat.getSell();
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart)>0) { //在这个区间的sell必需都为0000
            LOG.info("座位{}在本次车次区间{}~{},{}号车厢{}排{}列号座位已经售过票，不可选中该座位",dailyTrainSeat.getCarriageIndex()
                    ,startIndex,endIndex,dailyTrainSeat.getCarriageIndex(),dailyTrainSeat.getRow(),dailyTrainSeat.getCol());
            return false;
        }else {
            String curSell = sellPart.replace('0', '1');
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);// 补充之后变成几位
            curSell = StrUtil.fillAfter(curSell,'0',sell.length()); // 尾部补零
            // 当前的售票信息curSell和库里的已售sell按位或,即可得到座位售后的票的信息
            //这里转换为整型会丢失前面的0  15（01111）
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            // 1111-->会丢失一个0
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            newSell = StrUtil.fillBefore(newSell, '0',sell.length());
            LOG.info("{}号车厢{}排{}列号座位,原来的sell={},现在的售卖信息sell={}",
                    dailyTrainSeat.getCarriageIndex(),dailyTrainSeat.getRow(),dailyTrainSeat.getCol(),sell,newSell);
            dailyTrainSeat.setSell(newSell);
            return true;
        }
    }

    private   void reduceTickets(DailyTrainTicket dailyTrainTicket, SeatTypeEnum seatTypeEnum) {
        switch (seatTypeEnum){
            case YDZ -> {
                int countLeft= dailyTrainTicket.getYdz()-1;
                // 库存拦截
                if (countLeft<0){
                    throw new  MyException(40000,"余票不足");
                }
                dailyTrainTicket.setYdz(countLeft);
            }
            case EDZ -> {
                int countLeft= dailyTrainTicket.getEdz()-1;
                if (countLeft<0){
                    throw new  MyException(40000,"余票不足");
                }
                dailyTrainTicket.setEdz(countLeft);
            }
            case YW -> {
                int countLeft= dailyTrainTicket.getYw()-1;
                if (countLeft<0){
                    throw new  MyException(40000,"余票不足");
                }
                dailyTrainTicket.setYw(countLeft);
            }
            case RW -> {
                int countLeft= dailyTrainTicket.getRw()-1;
                if (countLeft<0){
                    throw new  MyException(40000,"余票不足");
                }
                dailyTrainTicket.setRw(countLeft);
            }
        }
    }
}




