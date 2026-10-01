package vn.iotstar.services;

import vn.iotstar.utils.BusinessException_24142116;

public interface IOrderService_24142116 {

    
    int placeOrder(String username, String receiverName, String phone, String address, String note)
            throws BusinessException_24142116;
}
