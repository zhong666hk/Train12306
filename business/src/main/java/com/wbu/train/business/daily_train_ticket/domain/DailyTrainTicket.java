package com.wbu.train.business.daily_train_ticket.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 余票信息
 * @TableName daily_train_ticket
 */
@TableName(value ="daily_train_ticket")
@Data
public class DailyTrainTicket implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 出发站拼音
     */
    private String startPinyin;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 出发站序|本站是整个车次的第几站
     */
    private Integer startIndex;

    /**
     * 到达站
     */
    private String end;

    /**
     * 到达站拼音
     */
    private String endPinyin;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 到站站序|本站是整个车次的第几站
     */
    private Integer endIndex;

    /**
     * 一等座余票
     */
    private Integer ydz;

    /**
     * 一等座票价
     */
    private BigDecimal ydzPrice;

    /**
     * 二等座余票
     */
    private Integer edz;

    /**
     * 二等座票价
     */
    private BigDecimal edzPrice;

    /**
     * 软卧余票
     */
    private Integer rw;

    /**
     * 软卧票价
     */
    private BigDecimal rwPrice;

    /**
     * 硬卧余票
     */
    private Integer yw;

    /**
     * 硬卧票价
     */
    private BigDecimal ywPrice;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
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
        DailyTrainTicket other = (DailyTrainTicket) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getTrainCode() == null ? other.getTrainCode() == null : this.getTrainCode().equals(other.getTrainCode()))
            && (this.getStart() == null ? other.getStart() == null : this.getStart().equals(other.getStart()))
            && (this.getStartPinyin() == null ? other.getStartPinyin() == null : this.getStartPinyin().equals(other.getStartPinyin()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getStartIndex() == null ? other.getStartIndex() == null : this.getStartIndex().equals(other.getStartIndex()))
            && (this.getEnd() == null ? other.getEnd() == null : this.getEnd().equals(other.getEnd()))
            && (this.getEndPinyin() == null ? other.getEndPinyin() == null : this.getEndPinyin().equals(other.getEndPinyin()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getEndIndex() == null ? other.getEndIndex() == null : this.getEndIndex().equals(other.getEndIndex()))
            && (this.getYdz() == null ? other.getYdz() == null : this.getYdz().equals(other.getYdz()))
            && (this.getYdzPrice() == null ? other.getYdzPrice() == null : this.getYdzPrice().equals(other.getYdzPrice()))
            && (this.getEdz() == null ? other.getEdz() == null : this.getEdz().equals(other.getEdz()))
            && (this.getEdzPrice() == null ? other.getEdzPrice() == null : this.getEdzPrice().equals(other.getEdzPrice()))
            && (this.getRw() == null ? other.getRw() == null : this.getRw().equals(other.getRw()))
            && (this.getRwPrice() == null ? other.getRwPrice() == null : this.getRwPrice().equals(other.getRwPrice()))
            && (this.getYw() == null ? other.getYw() == null : this.getYw().equals(other.getYw()))
            && (this.getYwPrice() == null ? other.getYwPrice() == null : this.getYwPrice().equals(other.getYwPrice()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getTrainCode() == null) ? 0 : getTrainCode().hashCode());
        result = prime * result + ((getStart() == null) ? 0 : getStart().hashCode());
        result = prime * result + ((getStartPinyin() == null) ? 0 : getStartPinyin().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getStartIndex() == null) ? 0 : getStartIndex().hashCode());
        result = prime * result + ((getEnd() == null) ? 0 : getEnd().hashCode());
        result = prime * result + ((getEndPinyin() == null) ? 0 : getEndPinyin().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getEndIndex() == null) ? 0 : getEndIndex().hashCode());
        result = prime * result + ((getYdz() == null) ? 0 : getYdz().hashCode());
        result = prime * result + ((getYdzPrice() == null) ? 0 : getYdzPrice().hashCode());
        result = prime * result + ((getEdz() == null) ? 0 : getEdz().hashCode());
        result = prime * result + ((getEdzPrice() == null) ? 0 : getEdzPrice().hashCode());
        result = prime * result + ((getRw() == null) ? 0 : getRw().hashCode());
        result = prime * result + ((getRwPrice() == null) ? 0 : getRwPrice().hashCode());
        result = prime * result + ((getYw() == null) ? 0 : getYw().hashCode());
        result = prime * result + ((getYwPrice() == null) ? 0 : getYwPrice().hashCode());
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
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", start=").append(start);
        sb.append(", startPinyin=").append(startPinyin);
        sb.append(", startTime=").append(startTime);
        sb.append(", startIndex=").append(startIndex);
        sb.append(", end=").append(end);
        sb.append(", endPinyin=").append(endPinyin);
        sb.append(", endTime=").append(endTime);
        sb.append(", endIndex=").append(endIndex);
        sb.append(", ydz=").append(ydz);
        sb.append(", ydzPrice=").append(ydzPrice);
        sb.append(", edz=").append(edz);
        sb.append(", edzPrice=").append(edzPrice);
        sb.append(", rw=").append(rw);
        sb.append(", rwPrice=").append(rwPrice);
        sb.append(", yw=").append(yw);
        sb.append(", ywPrice=").append(ywPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}