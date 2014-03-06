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

import com.kinetic.inventory.dao.UserDao;
import com.kinetic.inventory.model.User;
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


@Controller
@RequestMapping("/admin/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = {"", "/list"})
    private String list(Model model) {
        model.addAttribute("list", userDao.list());
        return "admin/user/list";
    }

    @RequestMapping("/newUser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/newUser";
    }

    @RequestMapping("/create")
    public String createUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/user/newUser";
        }
        log.debug("{}", user);
        user = userDao.createUser(user);

        redirectAttributes.addFlashAttribute("message", "The User " + user.getFullName() + " has been created");

        return "redirect:/admin/user/see/" + user.getUsername()+"/";
    }

    @RequestMapping("/see/{username}")
    public String see(@PathVariable String username, Model model) {
        User user = userDao.getUser(username);
        log.debug("{}", user);
        model.addAttribute("user", user);
        return "admin/user/see";
    }

    @RequestMapping("/delete/{username}/")
    public String delete(@PathVariable String username, Model model, RedirectAttributes redirectAttributes) {
         log.debug("deleting" );
        User user = userDao.getUser(username);
        userDao.deleteUser(user);
        redirectAttributes.addFlashAttribute("message", "The user " + username + " has been deleted");
        return "redirect:/admin/user";
    }
}
