package vip.sujianfeng.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * author SuJianFeng
 * createTime  2022/10/8
 * Description
 **/
@ApiModel("payOrderStatus")
public class PayOrderStatus {
    private String payOrderId;
    private String message;
    private int payStatus;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
