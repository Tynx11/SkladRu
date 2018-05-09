package ru.kpfu.itis.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.persistence.model.Product;
import ru.kpfu.itis.persistence.model.ProductQuantity;
import ru.kpfu.itis.persistence.model.Warehouse;
import ru.kpfu.itis.service.ProductService;
import ru.kpfu.itis.service.WarehouseService;
import ru.kpfu.itis.web.dto.ProductQuantityDto;
import ru.kpfu.itis.web.dto.QuantityDtoWrapper;
import ru.kpfu.itis.web.dto.WarehouseDto;

import javax.validation.Valid;
import java.util.List;

@Controller
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductService productService;


}
