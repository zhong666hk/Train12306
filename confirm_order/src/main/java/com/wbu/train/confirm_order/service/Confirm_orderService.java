package com.wbu.train.confirm_order.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.confirm_order.domain.Confirm_order;
import com.wbu.train.confirm_order.req.Confirm_orderQueryReq;
import com.wbu.train.confirm_order.req.Confirm_orderSaveReq;
import com.wbu.train.confirm_order.resp.Confirm_orderQueryResp;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【confirm_order(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface Confirm_orderService extends IService<Confirm_order> {
    public boolean saveConfirm_order(Confirm_orderSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<Confirm_orderQueryResp> queryConfirm_orders(Confirm_orderQueryReq req);

    /**
     * 删除confirm_order 通过id
     * @param id
     */
    public boolean  deleteById(Long id);
}