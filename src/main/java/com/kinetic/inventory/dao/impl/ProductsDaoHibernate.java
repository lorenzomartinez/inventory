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
import com.kinetic.inventory.dao.ProductsDao;
import com.kinetic.inventory.model.Products;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author martinezl
 */
@Repository
@Transactional
public class ProductsDaoHibernate extends BaseDao implements ProductsDao {

    private Serializable products;

    @Override
    @Transactional(readOnly = true)
    public Products getProduct(Long id) {
        Products product = (Products) currentSession().get(Products.class, id);
        return product;
    }

    @Override
    public Products createProduct(Products product) {
        currentSession().save(product);
        return product;
    }

    @Override
    public void deleteProduct(Products product) {
        currentSession().delete(product);
    }

    @Override
    public Products editProduct(Products product) {
        currentSession().update(product);
        currentSession().flush();
        return product;
    }

    @Override
    public List<Products> list() {
        Query query = currentSession().createQuery("select p from Products as p ");
        return query.list();
    }
}
