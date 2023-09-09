package vip.sujianfeng.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * author SuJianFeng
 * createTime  2022/10/8
 * @Description
 **/
@ApiModel("支付订单状态")
public class PayOrderStatus {
    @ApiModelProperty("支付订单id")
    private String payOrderId;
    @ApiModelProperty("支付信息（如支付错误信息等）")
    private String message;
    @ApiModelProperty("支付状态（0: 支付成功，1: 待支付，2: 支付失败）")
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
