package ru.kpfu.itis.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.persistence.model.Category;
import ru.kpfu.itis.persistence.model.Product;
import ru.kpfu.itis.persistence.model.ProductQuantity;
import ru.kpfu.itis.persistence.model.Warehouse;
import ru.kpfu.itis.service.CategoryService;
import ru.kpfu.itis.service.ProductService;
import ru.kpfu.itis.service.WarehouseService;
import ru.kpfu.itis.support.web.MessageHelper;
import ru.kpfu.itis.web.dto.*;
import ru.kpfu.itis.web.error.ProductNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/admin")
    String admin() {
        return "admin/adminPanel";
    }

    @ModelAttribute("module")
    String model() {
        return "admin";
    }

    @RequestMapping(value = "/admin/category/add", method = RequestMethod.GET)
    public String addCategoryPage(Model model) {
        model.addAttribute("categoryForm", new CategoryDto());
        return "admin/addCategory";
    }

    @RequestMapping(value = "/admin/category/add", method = RequestMethod.POST)
    public String addCategory(@ModelAttribute("categoryForm") @Valid CategoryDto categoryDto, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return "admin/addCategory";
        }
        Category category = categoryService.findByName(categoryDto.getName());
        if (category != null) {
            errors.rejectValue("name", null, "Category already exists");
            return "admin/addCategory";
        }
        category = new Category();
        category.setName(categoryDto.getName());
        categoryService.addCategory(category);
        MessageHelper.addSuccessAttribute(ra, "Category added");
        return "redirect:/admin/category/add";
    }

    @RequestMapping(value = "/admin/product/add", method = RequestMethod.GET)
    public String addProductPage(Model model) {
        model.addAttribute("productForm", new ProductDto());
        return "admin/addProduct";
    }

    @RequestMapping(value = "/admin/product/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("productForm") @Valid ProductDto productDto, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "admin/addProduct";
        }

        Product product = new Product();
        Category category = categoryService.findById(productDto.getCategory());
        category.getProductList().add(product);
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        List<Warehouse> warehouseList = warehouseService.findAll();

        for (Warehouse warehouse : warehouseList) {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.setWarehouse(warehouse);
            productQuantity.setQuantity(0);
            product.getProductQuantities().add(productQuantity);
        }
        productService.save(product);
        return "redirect:/admin";
    }


    @RequestMapping(value = "/admin/product", method = RequestMethod.GET)
    public String allProductsPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("allProducts", products);
        return "admin/productList";
    }

    @RequestMapping(value = "/admin/product/{id}/edit", method = RequestMethod.GET)
    public String editProductPage(Model model, @PathVariable("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory().getId());
        model.addAttribute("productForm", productDto);
        model.addAttribute("productId", product.getId());
        return "admin/editProduct";
    }

    @RequestMapping(value = "/admin/product/{id}/edit", method = RequestMethod.POST)
    public String editProduct(@ModelAttribute("productForm") @Valid ProductDto productDto, Errors errors, @PathVariable Long id, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("productId", id);
            return "admin/editProduct";
        }
        Product product = productService.findById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        productService.update(product);
        return "redirect:/admin";
    }


    @RequestMapping(value = "/admin/warehouse/{id}", method = RequestMethod.GET)
    public String productsInWarehouse(@PathVariable Long id, Model model) {
        QuantityDtoWrapper quantityDtoWrapper = new QuantityDtoWrapper();
        Warehouse warehouse = warehouseService.findById(id);
        List<ProductQuantity> productQuantities = warehouseService.findProductsQuantityOnWarehouse(warehouse);
        for (ProductQuantity productQuantity : productQuantities) {
            ProductQuantityDto productQuantityDto = new ProductQuantityDto();
            productQuantityDto.setId(productQuantity.getId());
            productQuantityDto.setName(productQuantity.getProduct().getName());
            productQuantityDto.setProductId(productQuantity.getProduct().getId());
            productQuantityDto.setDescription(productQuantity.getProduct().getDescription());
            productQuantityDto.setQuantity(productQuantity.getQuantity());
            productQuantityDto.setPrice(productQuantity.getProduct().getPrice());
            quantityDtoWrapper.getProductQuantities().add(productQuantityDto);
        }
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("quantityWrapper", quantityDtoWrapper);
        return "admin/warehouseProducts";
    }

    @RequestMapping(value = "/admin/warehouse/{id}", method = RequestMethod.POST)
    public String changeQuantity(@ModelAttribute("quantityWrapper") @Valid QuantityDtoWrapper quantityDtoWrapper, Errors errors, @PathVariable Long id, Model model) {
        if (errors.hasErrors()) {
            Warehouse warehouse = warehouseService.findById(id);
            model.addAttribute("warehouse", warehouse);
            return "admin/warehouseProducts";
        }
        for (ProductQuantityDto productQuantityDto : quantityDtoWrapper.getProductQuantities()) {
            ProductQuantity productQuantity = warehouseService.findQuantityById(productQuantityDto.getId());
            productQuantity.setQuantity(productQuantityDto.getQuantity());
            warehouseService.saveQuantity(productQuantity);
        }
        return "redirect:/admin/warehouse/" + id;
    }

    @RequestMapping(value = "/admin/warehouse", method = RequestMethod.GET)
    public String warehouseList(Model model) {
        List<Warehouse> warehouseList = warehouseService.findAll();
        model.addAttribute("warehouseList", warehouseList);
        return "admin/warehouseList";
    }

    @RequestMapping(value = "/admin/warehouse/add", method = RequestMethod.GET)
    public String warehousePage(Model model) {
        model.addAttribute("warehouseForm", new WarehouseDto());
        return "admin/addWarehouse";
    }

    @RequestMapping(value = "/admin/warehouse/add", method = RequestMethod.POST)
    public String warehouseAdd(@ModelAttribute("warehouseForm") @Valid WarehouseDto warehouseDto, Errors errors,
                               RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return "admin/addWarehouse";
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setCity(warehouseDto.getCity());
        warehouse.setAddress(warehouseDto.getAddress());

        List<Product> products = productService.findAll();
        for (Product product : products) {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setWarehouse(warehouse);
            productQuantity.setProduct(product);
            productQuantity.setQuantity(0);
            warehouse.getProductQuantities().add(productQuantity);
        }
        warehouseService.addWarehouse(warehouse);
        return "admin/adminPanel";
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
}
