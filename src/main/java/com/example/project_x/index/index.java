package com.example.project_x.index;

import com.example.project_x.ProjectXApplication;
import com.example.project_x.apis.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.Date;

import static com.example.project_x.apis.Top.getAllStaffCount;

@Controller
public class index {
    public static Top top = ProjectXApplication.Top;
    public static Connection connection = top.connection;

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/director")
    public String index2(Model model){
        new ProjectXApplication();
        top = ProjectXApplication.Top;
        String res_getAllStaffCount = Top.getAllStaffCount();
        int res_getContractCount = Top.getContractCount();
        int res_getOrderCount = Top.getOrderCount();
        int res_getNeverSoldProductCount = Top.getNeverSoldProductCount();
        String res_getFavoriteProductModel = Top.getFavoriteProductModel();
        String res_getContractInfo = Top.getContractInfo("CSE0000101");
        model.addAttribute("getAllStaffCount", res_getAllStaffCount);
        model.addAttribute("getContractCount", res_getContractCount);
        model.addAttribute("getOrderCount", res_getOrderCount);
        model.addAttribute("getNeverSoldProductCount", res_getNeverSoldProductCount);
        model.addAttribute("getContractInfo", res_getContractInfo);
        model.addAttribute("getFavoriteProductModel", res_getFavoriteProductModel);
        return "director";
    }

    @PostMapping (value = "/stockin")
    public String index3(String id, String supply_center, String product_model, String supply_staff, String data, String purchase_price, String quantity){
        Top.stockInSingle(Integer.parseInt(id), supply_center, product_model, Integer.parseInt(supply_staff), Date.valueOf(data), Integer.parseInt(purchase_price), Integer.parseInt(quantity));
        return "stockin";
    }



}
