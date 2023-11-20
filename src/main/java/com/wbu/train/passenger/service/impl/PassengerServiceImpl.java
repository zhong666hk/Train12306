package com.wbu.train.passenger.service.impl;

import com.wbu.train.passenger.entity.Passenger;
import com.wbu.train.passenger.mapper.PassengerMapper;
import com.wbu.train.passenger.service.IPassengerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 乘车人 服务实现类
 * </p>
 *
 * @author zzb
 * @since 2023-11-19
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger> implements IPassengerService {

}
