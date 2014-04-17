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
package com.kinetic.inventory.dao.impl;

import com.kinetic.inventory.dao.BaseDao;
import com.kinetic.inventory.dao.UserDao;
import com.kinetic.inventory.model.Role;
import com.kinetic.inventory.model.User;
import java.util.List;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Spring annotation that indicates the following class is a data access object (DAO)
@Repository
// Spring annotation to load the bean definitions
@Transactional
public class UserDaoHibernate extends BaseDao implements UserDao {

    // Password encoder to save password in encrypted form instead of plaintext
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Read only method, returns user. Has a comment for debugging purposes
    @Override
    @Transactional(readOnly = true)
    public User getUser(String username) {
        User user = (User) currentSession().get(User.class, username);
//        log.debug("user: {}", user);
        return user;
    }

    // Read only method to read the role(s) for the specific user
    @Override
    @Transactional(readOnly = true)
    public Role getRole(String authority) {
        return (Role) currentSession().get(Role.class, authority);
    }

    // Creates the role for the specific user
    @Override
    public Role createRole(Role role) {
        currentSession().save(role);
        return role;
    }

    // Method that encrypts the password and creates the user
    @Override
    public User createUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        currentSession().save(user);
        return user;
    }

    // Method that returns the array of the object user
    @Override
    public List<User> list() {
        Query query = currentSession().createQuery("select u from User as u where u.accountExpired=false");
        return query.list();
    }

    // The method that just simply deletes the user
    @Override
    public void deleteUser(User user) {
        currentSession().delete(user);
    }
}
