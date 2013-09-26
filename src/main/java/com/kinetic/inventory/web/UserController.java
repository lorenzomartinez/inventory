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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 *
 * @author martinezl
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {
        private static final Logger log = LoggerFactory.getLogger(UserController.class);
        @Autowired
        private UserDao userDao;

    @RequestMapping(value = {"", "/list"})
    public String list(Model model) {
//        List articles = articleDao.list();
        model.addAttribute("user", userDao.list());
        return "admin/user/list";
    }
    
    
            
    
        
//            @RequestMapping(value={"","/create"})
//    public String createUser(Model model){
//                log.debug("new user");
//                User user = new User();
//                model.addAttribute("user", user);
//                model.addAttribute("roles", role);
//        user = userDao.createUser(user);
//  
//        redirectAttributes.addFlashAttribute("message", "The User "+user.getFullName()+" has been created");
//        
//        return "redirect:/user/see/"+user.getUsername();    
//    }
//                @RequestMapping("/nuevo")
//    public String nuevo(Model modelo) {
//        log.debug("Nuevo usuario");
//        List<Rol> roles = obtieneRoles();
//        Usuario usuario = new Usuario();
//        modelo.addAttribute("usuario", usuario);
//        modelo.addAttribute("roles", roles);
//        List<Ejercicio> ejercicios = usuarioDao.obtieneEjercicios(ambiente
//                .obtieneUsuario().getEmpresa().getOrganizacion().getId());
//        modelo.addAttribute("ejercicios", ejercicios);
//        modelo.addAttribute("enviaCorreo", Boolean.TRUE);
//        return "admin/usuario/nuevo";
//    }
            
        
    
}
