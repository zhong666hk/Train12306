package com.wbu.train.member.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 车票
 * @TableName ticket
 */
@TableName(value ="ticket")
@Data
public class Ticket implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    /**
     *  车次编号
     */
    private String trainCode;

    /**
     *  箱序
     */
    private Integer carriageIndex;

    /**
     *  排号|0102
     */
    @TableField("`row`")
    private String row;

    /**
     *  列号|枚举[SeatColEnum]
     */
    @TableField("`col`")
    private String col;

    /**
     * 出发站
     */
    private String start;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 到达站
     */
    private String end;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 座位类型I枚举[SeatTypeEnum]
     */
    private String seatType;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     *  修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Ticket other = (Ticket) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getPassengerId() == null ? other.getPassengerId() == null : this.getPassengerId().equals(other.getPassengerId()))
            && (this.getPassengerName() == null ? other.getPassengerName() == null : this.getPassengerName().equals(other.getPassengerName()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getTrainCode() == null ? other.getTrainCode() == null : this.getTrainCode().equals(other.getTrainCode()))
            && (this.getCarriageIndex() == null ? other.getCarriageIndex() == null : this.getCarriageIndex().equals(other.getCarriageIndex()))
            && (this.getRow() == null ? other.getRow() == null : this.getRow().equals(other.getRow()))
            && (this.getCol() == null ? other.getCol() == null : this.getCol().equals(other.getCol()))
            && (this.getStart() == null ? other.getStart() == null : this.getStart().equals(other.getStart()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEnd() == null ? other.getEnd() == null : this.getEnd().equals(other.getEnd()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getSeatType() == null ? other.getSeatType() == null : this.getSeatType().equals(other.getSeatType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getPassengerId() == null) ? 0 : getPassengerId().hashCode());
        result = prime * result + ((getPassengerName() == null) ? 0 : getPassengerName().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getTrainCode() == null) ? 0 : getTrainCode().hashCode());
        result = prime * result + ((getCarriageIndex() == null) ? 0 : getCarriageIndex().hashCode());
        result = prime * result + ((getRow() == null) ? 0 : getRow().hashCode());
        result = prime * result + ((getCol() == null) ? 0 : getCol().hashCode());
        result = prime * result + ((getStart() == null) ? 0 : getStart().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEnd() == null) ? 0 : getEnd().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getSeatType() == null) ? 0 : getSeatType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", passengerId=").append(passengerId);
        sb.append(", passengerName=").append(passengerName);
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", carriageIndex=").append(carriageIndex);
        sb.append(", row=").append(row);
        sb.append(", col=").append(col);
        sb.append(", start=").append(start);
        sb.append(", startTime=").append(startTime);
        sb.append(", end=").append(end);
        sb.append(", endTime=").append(endTime);
        sb.append(", seatType=").append(seatType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}