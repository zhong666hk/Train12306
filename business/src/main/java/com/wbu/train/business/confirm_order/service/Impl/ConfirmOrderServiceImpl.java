package com.wbu.train.business.confirm_order.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.confirm_order.req.ConfirmOrderDoReq;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.enums.ConfirmOrderStatusEnum;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.confirm_order.domain.ConfirmOrder;
import com.wbu.train.business.confirm_order.mapper.ConfirmOrderMapper;
import com.wbu.train.business.confirm_order.req.ConfirmOrderQueryReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderSaveReq;
import com.wbu.train.business.confirm_order.resp.ConfirmOrderQueryResp;
import com.wbu.train.business.confirm_order.service.ConfirmOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        confirmOrder.setTickets(JSON.toJSONString(req.getTickets()));
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        if (!this.save(confirmOrder)) {
            throw new MyException(4000,"插入异常");
        }

        // 三、查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUniqueKey(reqDate, reqTrainCode, reqStart, reqEnd);
        // 四、扣减余票数量，并判断余票是否足够

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
}




