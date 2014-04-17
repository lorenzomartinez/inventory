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
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Spring annotation that indicates the following class is a data access object (DAO)
@Repository
// Spring annotation to load the bean definitions
@Transactional
public class ProductsDaoHibernate extends BaseDao implements ProductsDao {

    // Read only method that returns the product
    @Override
    @Transactional(readOnly = true)
    public Products getProduct(Long id) {
        Products product = (Products) currentSession().get(Products.class, id);
        return product;
    }

    // Method to create and save the object product
    @Override
    public Products createProduct(Products product) {
        currentSession().save(product);
        return product;
    }

    // Method called when we need to delete the item
    @Override
    public void deleteProduct(Products product) {
        currentSession().delete(product);
    }

    // Method called when the product needs editing and returns the updated product
    @Override
    public Products editProduct(Products product) {
        currentSession().update(product);
        currentSession().flush();
        return product;
    }

    // Method that returns the array of Products
    @Override
    public List<Products> list() {
        Query query = currentSession().createQuery("select p from Products as p ");
        return query.list();
    }

    /*
     * This method allows search as you type functionality for products 
     * by manufactuere, model, or desciption. Returns a total of 10 results
     */
    @Override
    public List<Products> search(String term) {
        Disjunction props = Restrictions.disjunction();
        props.add(Restrictions.ilike("manufacturer", term, MatchMode.ANYWHERE));
        props.add(Restrictions.ilike("model", term, MatchMode.ANYWHERE));
        props.add(Restrictions.ilike("description", term, MatchMode.ANYWHERE));        
        Criteria criteria = currentSession().createCriteria(Products.class);
        criteria.add(props);
        criteria.setMaxResults(10);
        return criteria.list();
    }
}
