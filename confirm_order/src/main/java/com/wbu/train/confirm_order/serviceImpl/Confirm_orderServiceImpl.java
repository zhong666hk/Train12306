package com.wbu.train.generator.ftl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.confirm_order.domain.Confirm_order;
import com.wbu.train.confirm_order.mapper.Confirm_orderMapper;
import com.wbu.train.confirm_order.req.Confirm_orderQueryReq;
import com.wbu.train.confirm_order.req.Confirm_orderSaveReq;
import com.wbu.train.confirm_order.resp.Confirm_orderQueryResp;
import com.wbu.train.confirm_order.service.Confirm_orderService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 钟正保
 * @description 针对表【confirm_order(乘车人)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class Confirm_orderServiceImpl extends ServiceImpl<Confirm_orderMapper, Confirm_order>
        implements Confirm_orderService {

    @Override
    public boolean saveConfirm_order(Confirm_orderSaveReq req) {
        DateTime date = DateUtil.dateSecond(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Confirm_order confirm_order = BeanUtil.copyProperties(req, Confirm_order.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(confirm_order.getId())){
            confirm_order.setId(SnowUtil.getSnowflakeNextId());
            confirm_order.setCreateTime(date);
            confirm_order.setUpdateTime(date);
            return this.save(confirm_order);
        }// id不为空说明是修改的操作
        else {
            confirm_order.setUpdateTime(date);
            return this.updateById(confirm_order);
        }
    }

    @Override
    public Page<Confirm_orderQueryResp> queryConfirm_orders(Confirm_orderQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Confirm_order> confirm_orderQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<Confirm_order> page = this.page(new Page<>(req.getPage(), req.getSize()), confirm_orderQueryWrapper);
        Page<Confirm_orderQueryResp> confirm_orderQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,confirm_orderQueryRespPage);
        return confirm_orderQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }
}




