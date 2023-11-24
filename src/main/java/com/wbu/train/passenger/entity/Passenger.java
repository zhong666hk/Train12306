package com.wbu.train.passenger.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
/**
 * <p>
 * 乘车人
 * </p>
 *
 * @author zzb
 * @since 2023-11-20
 */
@Data
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */

    private Long id;

    /**
     * 会员id
     */

    private Long memberId;

    /**
     * 姓名
     */

    private String name;

    /**
     * 身份证
     */

    private String idCard;

    /**
     * 旅客类型|枚举[PassengerTypeEnum]
     */

    private String type;

    /**
     * 新增时间
     */

    private LocalDateTime createTime;

    /**
     * 修改时间
     */

    private LocalDateTime updateTime;
}
