package ru.kpfu.itis.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.persistence.model.Order;
import ru.kpfu.itis.persistence.model.Product;
import ru.kpfu.itis.persistence.model.User;
import ru.kpfu.itis.service.OrderService;
import ru.kpfu.itis.service.ProductService;
import ru.kpfu.itis.service.UserService;
import ru.kpfu.itis.util.GenericResponse;
import ru.kpfu.itis.web.error.ProductNotFoundException;
import ru.kpfu.itis.web.vo.Cart;
import ru.kpfu.itis.web.vo.ProductInCart;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/cart/add", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse addToCart(@RequestParam("prodId") Long id, Principal principal, Model model) {
        Product product = productService.findById(id);
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Order pendingOrder = orderService.createPendingOrderIfNotExists(user);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + id);
        }
//        cart.addOneNewProduct(product);
        orderService.addNewProductToOrder(pendingOrder, product);
        return new GenericResponse("product added");
    }

    @RequestMapping(value = "/cart/remove", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse removeFromCart(@RequestParam("prodId") Long id, Principal principal) {
        Product product = productService.findById(id);
        User user = userService.getUserByEmail(principal.getName());
        Order order = orderService.createPendingOrderIfNotExists(user);
        orderService.removeProduct(order, product);
        return new GenericResponse("product removed");
    }

    @RequestMapping(value = "/order/begin", method = RequestMethod.GET)
    public String beginOrder(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Order pendingOrder = orderService.createPendingOrderIfNotExists(user);
        model.addAttribute("cart", pendingOrder);
        return "order/begin";
    }

    @RequestMapping(value = "/order/checkout", method = RequestMethod.POST)
    public String update(@ModelAttribute ProductInCart productInCart, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Order order = orderService.createPendingOrderIfNotExists(user);
        Order updatedOrder = orderService.updateQuantityOrder(order, productInCart.getProductId(), productInCart.getCount());
//        model.addAttribute("cart", updatedOrder);
        return "redirect:/order/begin";
    }

    @RequestMapping(value = "/order/confirm", method = RequestMethod.POST)
    public String confirmOrder(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Order order = orderService.getPendingOrder(user);
        orderService.confirmOrder(order);
        return "redirect:/order";
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String showMyOrders(Model model, Principal principal) {
        List<Order> ordersByUser = orderService.getOrdersByUser(principal.getName());
        model.addAttribute("orders", ordersByUser);
        return "order/orders";
    }

}
