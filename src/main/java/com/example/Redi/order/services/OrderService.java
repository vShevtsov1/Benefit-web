package com.example.Redi.order.services;

import com.example.Redi.email.EmailService;
import com.example.Redi.logs.data.Logs;
import com.example.Redi.logs.enums.LogType;
import com.example.Redi.logs.service.LogsService;
import com.example.Redi.order.DTO.*;
import com.example.Redi.order.data.CreateOrderDTO;
import com.example.Redi.order.data.Order;
import com.example.Redi.order.enums.OrderCreateType;
import com.example.Redi.order.enums.OrderType;
import com.example.Redi.products.DTO.ProductsDTO;
import com.example.Redi.products.data.Product;
import com.example.Redi.products.services.ProductsRepo;
import com.example.Redi.users.DTO.UserDTO;
import com.example.Redi.users.data.User;
import com.example.Redi.users.services.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LogsService logsService;

    @Autowired
    private EmailService emailService;
    public CreateOrderResponseDTO createOrder(CreateOrderDTO createOrderDTO, String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return null;
        }
        Double sum = 0.0;
        List<Product> products = new ArrayList<>();
        for (ProductsOrderDTO productsOrderDTO : createOrderDTO.getProducts()) {
            Product product = productsRepo.findById(productsOrderDTO.getProduct()).get();
            if (product.getAvailableQuantity() >= productsOrderDTO.getQuantity()) {
                product.setAvailableQuantity(product.getAvailableQuantity() - productsOrderDTO.getQuantity());
                product.setPurchaseCount(product.getPurchaseCount() + productsOrderDTO.getQuantity());
                products.add(product);
                sum += productsOrderDTO.getQuantity() * product.getPrice();
            } else {
                return new CreateOrderResponseDTO("Some products are out of stock", OrderCreateType.NO_STOCK, null);
            }
        }
        if (user.getBonusCount() < sum) {
            return new CreateOrderResponseDTO("User doesn't have enough balance", OrderCreateType.INSUFFICIENT_BALANCE, null);
        }
        for (Product product : products) {
            productsRepo.save(product);
        }
        user.setBonusCount((int) (user.getBonusCount() - sum));
        userRepo.save(user);
        Order order = new Order(LocalDateTime.now(), createOrderDTO.getProducts(), sum, OrderType.NEW, user.getId(),orderRepo.count()+1);
        orderRepo.save(order);
        OrderFullDTO orderFullDTO = modelMapper.map(order, OrderFullDTO.class);
        List<ProductsOrderFullDTO> products_response = new ArrayList<>();
        for (ProductsOrderDTO productsOrderDTO : createOrderDTO.getProducts()) {
            products_response.add(new ProductsOrderFullDTO(modelMapper.map(productsRepo.findById(productsOrderDTO.getProduct()).get(), ProductsDTO.class), productsOrderDTO.getQuantity()));
        }
        orderFullDTO.setProducts(products_response);
        orderFullDTO.setUser(modelMapper.map(user, UserDTO.class));

        CompletableFuture.runAsync(() ->emailService.sendEmailOrder("s.batanin@redi.partners",String.format("%s %s (%s)", user.getName(), user.getSurname(), user.getEmail()),order.getTime(),user.getShippingAddress(),products_response,order.getSum()))
                .thenRun(() -> log.info("Email sent asynchronously!"))
                .exceptionally(ex -> {
                    log.error("Failed to send email", ex);
                    return null;
                });

        return new CreateOrderResponseDTO("order created successfully", OrderCreateType.CREATED, orderFullDTO);
    }

    public List<OrderFullDTO> getOrdersByStatus(List<String> status) {
        if (status.isEmpty()) {
            return orderRepo.getOrdersByStatus(Arrays.asList("NEW", "IN_PROGRESS", "NEEDS_APPROVAL", "SENT", "DELIVERED", "COMPLETED", "CANCELED"));
        } else {
            return orderRepo.getOrdersByStatus(status);
        }
    }

    public List<OrderFullDTO> getAllUsersOrders(String user_id) {
        return orderRepo.findByUser_id(user_id);
    }

    public List<ProductQuantitySold> calculateProductQuantitySoldBetweenDates(Date startDate, Date endDate) {
        return orderRepo.calculateProductQuantitySoldBetweenDates(startDate, endDate);
    }

    public List<ProductQuantitySold> calculateProductQuantitySoldBetweenDates(
            Date startDate,
            Date endDate,
            String productId
    ) {
        return orderRepo.calculateProductQuantitySoldBetweenDates(startDate, endDate, productId);
    }

    public Order changeOrderStatus(String orderId, OrderType type, String email) {
        Order order = orderRepo.findById(orderId).get();
        if (order == null) {
            return null;
        }
        String logMessage = String.format("Admin %s requested to change order with id  status from %s to %s", email, orderId, order.getOrderType(), type);

        order.setOrderType(type);
        orderRepo.save(order);
        logsService.createLog(new Logs(LogType.ORDER, email, LocalDateTime.now(), logMessage));
        return order;
    }

    public Order cancelOrder(String orderId, String email) {
        Order order = orderRepo.findById(orderId).get();
        if (order == null) {
            return null;
        }
        User user = userRepo.findById(order.getUser_id()).get();
        for (ProductsOrderDTO productsOrderDTO : order.getProducts()) {
            Product product = productsRepo.findById(productsOrderDTO.getProduct()).get();
            product.setAvailableQuantity(product.getAvailableQuantity() + productsOrderDTO.getQuantity());
            product.setPurchaseCount(product.getPurchaseCount() - productsOrderDTO.getQuantity());
            productsRepo.save(product);
        }
        user.setBonusCount((int) (user.getBonusCount() + order.getSum()));
        userRepo.save(user);
        order.setOrderType(OrderType.CANCELED);
        orderRepo.save(order);
        logsService.createLog(new Logs(LogType.ORDER, email, LocalDateTime.now(), "Admin request to cancel the order with id" + orderId));
        return order;
    }

    public List<OrderFullDTO> getAllUserOrders(String email) {
        User user = userRepo.findByEmail(email);
        return orderRepo.findByUser_id(user.getId());
    }

    public List<OrderFullDTO> findByUser_idAndTimeRange(String email, Date fromDate, Date toDate) {
        User user = userRepo.findByEmail(email);
        return orderRepo.findByUser_idAndTimeRange(user.getId(), fromDate, toDate);
    }
}
