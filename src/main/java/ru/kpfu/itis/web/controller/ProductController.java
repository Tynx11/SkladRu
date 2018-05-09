package ru.kpfu.itis.web.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.persistence.model.*;
import ru.kpfu.itis.service.*;
import ru.kpfu.itis.web.dto.ProductDto;
import ru.kpfu.itis.web.dto.ProductQuantityDto;
import ru.kpfu.itis.web.dto.QuantityDtoWrapper;
import ru.kpfu.itis.web.error.ProductNotFoundException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @ModelAttribute("module")
    String model() {
        return "product";
    }


    @RequestMapping(value = "/product/getProducts", method = RequestMethod.GET)
    public @ResponseBody
    List<Product> getProductList(@RequestParam("name") String query) {
        List<Product> productList = productService.findByNameIgnoreCase(query);
        return productList;
    }



    @ModelAttribute("allCategories")
    public List<Category> allCategories() {
        List<Category> categories = new ArrayList<>();
        categories = categoryService.findAll();
        return categories;
    }


    @ModelAttribute("allWarehouses")
    public List<Warehouse> allWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        warehouses = warehouseService.findAll();
        return warehouses;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String showProductPage(Model model, @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "cat", required = false) Long categoryId, Principal principal) {
        List<ProductQuantityDto> quantityDtoList = new ArrayList<>();

        List<Product> products;
        if (name != null && !name.trim().equals("")) {
            products = productService.findByNameIgnoreCase(name);
        } else {
            products = productService.findAll();
        }

        if (categoryId != null) {
            products = products.stream().filter(product -> product.getCategory().getId().equals(categoryId)).collect(Collectors.toList());
            model.addAttribute("selectedCategory", categoryId);
        }

        for (Product product : products) {
            ProductQuantityDto productQuantityDto = new ProductQuantityDto();
            productQuantityDto.setProductId(product.getId());
            productQuantityDto.setName(product.getName());
            productQuantityDto.setDescription(product.getDescription());
            productQuantityDto.setPrice(product.getPrice());
            for (ProductQuantity productQuantity : product.getProductQuantities()) {
                productQuantityDto.addQuantity(productQuantity.getQuantity());
            }
            quantityDtoList.add(productQuantityDto);
        }
        model.addAttribute("productsList", quantityDtoList);

        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Order pendingOrder = orderService.createPendingOrderIfNotExists(user);
            model.addAttribute("cart", pendingOrder);
        }
        return "product/productList";
    }



}
