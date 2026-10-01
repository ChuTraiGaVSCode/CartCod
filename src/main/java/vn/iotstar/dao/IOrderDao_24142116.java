package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.OrderModel_24142116;

public interface IOrderDao_24142116 {

   
    int create(OrderModel_24142116 order, List<CartItemModel_24142116> items);
}
