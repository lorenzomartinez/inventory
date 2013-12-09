/*
 * The MIT License
 *
 * Copyright 2013 martinezl.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kinetic.inventory.web;

import com.kinetic.inventory.dao.ProductsDao;
import com.kinetic.inventory.model.Products;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author martinezl
 */
@Controller
@RequestMapping("/products")
public class ProductsController {

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    @Autowired
    private ProductsDao productsDao;
    
    @RequestMapping(value = {"", "/list"})
    public String list(Model model){
        model.addAttribute("list", productsDao.list());
        return "/products/list";
    }
    //new
    @RequestMapping("/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Products());
        return "/products/newProduct";
    }
    //create
    @RequestMapping("/create")
    public String createProduct(@Valid Products products, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "/products/newProduct";
        }
        products = productsDao.createProduct(products);
        redirectAttributes.addFlashAttribute("message", "The Product " + products.getModel() + " has been created");
        return "redirect:/products/see/" + products.getId()+"/";
    }
    //see
    @RequestMapping("/see/{id}")
    public String see(@PathVariable Long id, Model model) {
        Products product = productsDao.getProduct(id);
        model.addAttribute("product", product);
        return "/products/see";
    }
    //delete
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Products product = productsDao.getProduct(id);
        productsDao.deleteProduct(product);
        redirectAttributes.addFlashAttribute("message", "The product "+ id + " has been deleted");
        return "redirect:/products/";
    }
    //update
    /*
     
     */
}
