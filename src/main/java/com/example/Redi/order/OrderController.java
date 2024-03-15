package com.example.Redi.order;

import com.example.Redi.order.DTO.CreateOrderResponseDTO;
import com.example.Redi.order.DTO.OrderFullDTO;
import com.example.Redi.order.DTO.ProductQuantitySold;
import com.example.Redi.order.data.CreateOrderDTO;
import com.example.Redi.order.data.Order;
import com.example.Redi.order.enums.OrderType;
import com.example.Redi.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponseDTO> createNewOrder(@RequestBody CreateOrderDTO createOrderDTO, Authentication authentication){
        try {
            return new ResponseEntity<>(orderService.createOrder(createOrderDTO,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get-orders")
    public ResponseEntity<List<OrderFullDTO>> getOrders(@RequestBody List<String> statuses){
        try {
            return new ResponseEntity<>(orderService.getOrdersByStatus(statuses),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/user/orders")
    public ResponseEntity<List<OrderFullDTO>> getAllUserOrders(@RequestParam("user_id") String user_id){
        try {
            return new ResponseEntity<>(orderService.getAllUsersOrders(user_id),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/products-quantity-sold")
    public List<ProductQuantitySold> getProductQuantitySoldBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return orderService.calculateProductQuantitySoldBetweenDates(startDate, endDate);
    }
    @GetMapping("/product-quantity-sold")
    public List<ProductQuantitySold> getProductQuantitySoldBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("productId") String productId
    ) {
        return orderService.calculateProductQuantitySoldBetweenDates(startDate, endDate, productId);
    }

    @PostMapping("/admin/order/status")
    public ResponseEntity<Order> changeOrderStatus(@RequestParam(value = "order_id") String order_id, @RequestParam("OrderType")OrderType OrderType,Authentication authentication){
        try {
            return new ResponseEntity<>(orderService.changeOrderStatus(order_id,OrderType,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/cancel/order")
    public ResponseEntity<Order> cancelOrder(@RequestParam(value = "order_id") String orderId,Authentication authentication){
        try {
            return new ResponseEntity<>(orderService.cancelOrder(orderId,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderFullDTO>> getAllMyOrders(Authentication authentication){
        try {
            return new ResponseEntity<>(orderService.getAllUserOrders(authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/my-orders/user")
    public ResponseEntity<List<OrderFullDTO>> getAllMyOrdersFilter(Authentication authentication){
        try {
            return new ResponseEntity<>(orderService.getAllUserOrders(authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my-orders/filter")
    public List<OrderFullDTO> findByUserIdAndTimeRange(
           Authentication authentication,
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate
    ) {
        return orderService.findByUser_idAndTimeRange(authentication.getPrincipal().toString(), fromDate, toDate);
    }


}
