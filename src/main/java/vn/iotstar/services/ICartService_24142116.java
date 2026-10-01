package vn.iotstar.services;

import java.util.List;

import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.utils.BusinessException_24142116;

public interface ICartService_24142116 {

    List<CartItemModel_24142116> getCart(String username);

    long total(List<CartItemModel_24142116> items);

    int countQuantity(String username);

    
    void add(String username, String videoId, int qty) throws BusinessException_24142116;

    
    void updateQuantity(String username, String videoId, int qty) throws BusinessException_24142116;

    void remove(String username, String videoId);

    void clear(String username);
}
