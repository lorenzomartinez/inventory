/*
 * The MIT License
 *
 * Copyright 2014 martinezl.
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
import com.kinetic.inventory.dao.ItemDao;
import com.kinetic.inventory.model.Item;
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
public class ItemDaoHibernate extends BaseDao implements ItemDao {

    @Override
    public Item createItem(Item item) {
//        item.setInvoice(currentSession().get(Invoice.class, item.getInvoice().getId()));
        currentSession().refresh(item.getInvoice());
        currentSession().refresh(item.getProduct());
        currentSession().save(item);
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItem(Long id) {
        Item item = (Item) currentSession().get(Item.class, id);
        return item;
    }

    @Override
    public void deleteItem(Item item) {
        currentSession().delete(item);
    }

    @Override
    public List<Item> list() {
        Query query = currentSession().createQuery("select i from Item as i ");
        return query.list();
    }
    
}
