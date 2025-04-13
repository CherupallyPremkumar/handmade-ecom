package com.handmade.ecommerce.category.configuration.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import com.handmade.ecommerce.category.model.Category;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ChenileController(value = "categoryService", serviceName = "_categoryService_",
		healthCheckerName = "categoryHealthChecker")
public class CategoryController extends ControllerSupport{
	
    @PostMapping("/category")
    @SecurityConfig(authorities = {"some_premium_scope","test.premium"})
    public ResponseEntity<GenericResponse<Category>> save(
        HttpServletRequest httpServletRequest,
        @RequestBody Category entity){
        return process(httpServletRequest,entity);
        }

    @GetMapping("/category/{id}")
    @SecurityConfig(authorities = {"some_premium_scope","test.premium"})
    public ResponseEntity<GenericResponse<Category>> retrieve(
    HttpServletRequest httpServletRequest,
    @PathVariable("id") String id){
    return process(httpServletRequest,id);
    }
}
