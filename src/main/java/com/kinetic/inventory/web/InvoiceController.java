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

import com.kinetic.inventory.dao.ItemDao;
import com.kinetic.inventory.dao.ProductsDao;
import com.kinetic.inventory.dao.ClientDao;
import com.kinetic.inventory.dao.InvoiceDao;
import com.kinetic.inventory.model.Invoice;
import com.kinetic.inventory.model.Item;
import com.kinetic.inventory.model.Products;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Defines class as a controller in the MVC architecture
@Controller
/* The directory used by the controller will be /invoice and any mapping values hereinafter
 * will be assumed to be under the /invoice directory
 */
@RequestMapping("/invoice")
public class InvoiceController {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
    
    // Declaring and connecting to the Dao classes to use in this controller
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ProductsDao productsDao;
    @Autowired
    private ItemDao itemDao;

    // This method return the list of invoices
    @RequestMapping(value = {"", "/list"})
    public String list(Model model) {
        model.addAttribute("list", invoiceDao.list());
        return "/invoice/list";
    }

    // Method that takes us to the form to create a new invoice
    @RequestMapping("/newInvoice")
    public String newProduct(Model model) {
        model.addAttribute("clients", clientDao.list());
        model.addAttribute("invoice", new Invoice());
        return "/invoice/newInvoice";
    }

    // If any errors go back to the form, if good, show the created invoice
    @RequestMapping("/create")
    public String createInvoice(@Valid Invoice invoice, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Couldn't create invoice {}", bindingResult.getAllErrors());
            return "/invoice/newInvoice";
        }
        invoice = invoiceDao.createInvoice(invoice);
        redirectAttributes.addFlashAttribute("message", "The Invoice " + invoice.getId() + " has been created");
        return "redirect:/invoice/see/" + invoice.getId() + "/";
    }

    // Method that takes us to the page where the information on the invoice is displayed
    @RequestMapping("/see/{id}")
    public String see(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceDao.getInvoice(id);
        model.addAttribute("invoice", invoice);
        return "/invoice/see";
    }

    // Method to edit the invoice
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("clients", clientDao.list());
        Invoice invoice = invoiceDao.getInvoice(id);
        model.addAttribute("invoice", invoice);
        return "invoice/edit";
    }

    // Method to remove the invoice, returns us to the list of invoices
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Invoice invoice = invoiceDao.getInvoice(id);
        invoiceDao.deleteInvoice(invoice);
        redirectAttributes.addFlashAttribute("message", "The invoice " + id + " has been deleted");
        return "redirect:/invoice/";
    }

    // Method that deletes an item within an invoice
    @RequestMapping("/item/delete/{id}/")
    public String deleteItem(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Item item = itemDao.getItem(id);
        itemDao.deleteItem(item);
        redirectAttributes.addFlashAttribute("message", "The item " + id + " has been deleted");
        return "redirect:/invoice/see/"+item.getInvoice().getId();
    }
    
    // Method to add an item to the specif invoice
    @RequestMapping("/addItem/{invoiceId}")
    public String addItem(@PathVariable Long invoiceId, Model model) {
        Invoice invoice = invoiceDao.getInvoice(invoiceId);
        Item item = new Item();
        item.setInvoice(invoice);
        model.addAttribute("products", productsDao.list());
        model.addAttribute("item", item);
        return "invoice/addItem";
    }
    
    // Method to return the values for the producs of the search as you type functionality 
    @RequestMapping(value = "/products", params = {"term"}, produces = "application/json")
    @ResponseBody
    public List<Products> products(@RequestParam String term) {
        log.debug("Looking for products with {}", term);
        List<Products> products = productsDao.search(term);

        return products;
    }

    // Method that adds the new product to the invoice, if any errors it goes back to the form
    @RequestMapping("/addItem")
    public String saveItem(@Valid Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Couldn't create invoice {}", bindingResult.getAllErrors());
            return "/invoice/newInvoice";
        }

        item = itemDao.createItem(item);
        redirectAttributes.addFlashAttribute("message", "The Item " + item.getId() + " has been created");
        return "redirect:/invoice/see/" + item.getInvoice().getId() + "/";

    }
}
