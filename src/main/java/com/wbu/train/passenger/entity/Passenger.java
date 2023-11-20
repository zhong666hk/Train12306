package com.wbu.train.passenger.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 乘车人
 * </p>
 *
 * @author zzb
 * @since 2023-11-19
 */
@Schema(name = "Passenger", description = "$!{table.comment}")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "会员id")
    private Long memberId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "身份证")
    private String idCard;

    @Schema(description = "旅客类型|枚举[PassengerTypeEnum]")
    private String type;

    @Schema(description = "新增时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id = " + id +
            ", memberId = " + memberId +
            ", name = " + name +
            ", idCard = " + idCard +
            ", type = " + type +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}
