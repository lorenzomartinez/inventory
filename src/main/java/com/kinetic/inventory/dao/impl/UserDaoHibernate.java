/*
 * The MIT License
 *
 * Copyright 2013 J. David Mendoza <jdmendoza@swau.edu>.
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@swau.edu>
 */
@Repository
@Transactional
public class UserDaoHibernate extends BaseDao implements UserDao {

    @Override
    @Transactional(readOnly = true)
    public User getUser(String username) {
        return (User) currentSession().get(User.class, username);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRole(String authority) {
        return (Role) currentSession().get(Role.class, authority);
    }

    @Override
    public Role createRole(Role role) {
        currentSession().save(role);
        return role;
    }

    @Override
    public User createUser(User user) {
        currentSession().save(user);
        return user;
    }
    
}
