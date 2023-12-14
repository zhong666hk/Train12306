package com.wbu.train.business.confirm_order.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.confirm_order.domain.ConfirmOrder;
import com.wbu.train.business.confirm_order.req.ConfirmOrderDoReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderQueryReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderSaveReq;
import com.wbu.train.business.confirm_order.resp.ConfirmOrderQueryResp;

/**
* @author 钟正保
* @description 针对表【confirmOrder】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface ConfirmOrderService extends IService<ConfirmOrder> {
    public boolean saveConfirmOrder(ConfirmOrderSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<ConfirmOrderQueryResp> queryConfirmOrders(ConfirmOrderQueryReq req);

    boolean deleteById(Long id);

    void doConfirm(ConfirmOrderDoReq req);
}