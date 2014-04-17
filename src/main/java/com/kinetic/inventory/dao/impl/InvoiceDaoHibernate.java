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
import com.kinetic.inventory.dao.InvoiceDao;
import com.kinetic.inventory.model.Client;
import com.kinetic.inventory.model.Invoice;
import com.kinetic.inventory.model.Item;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Spring annotation that indicates the following class is a data access object (DAO)
@Repository
// Spring annotation to load the bean definitions
@Transactional
public class InvoiceDaoHibernate extends BaseDao implements InvoiceDao {

    // Creates an invoice object with the date, gets and updates the client.
    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setDateCreated(new Date());
        Client client = invoice.getClient();
        currentSession().refresh(client);
        invoice.setClient(client);
        currentSession().save(invoice);
        return invoice;
    }

    // Method to return the invoice and is read only
    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoice(Long id) {
        Invoice invoice = (Invoice) currentSession().get(Invoice.class, id);
        return invoice;
    }

    // Method to delete invoice, deletes each item recursevely if any
    @Override
    public void deleteInvoice(Invoice invoice) {
        for (Item item : invoice.getItems()) {
            currentSession().delete(item);
        }

        currentSession().delete(invoice);
    }

    // Returns the array of invoices
    @Override
    public List<Invoice> list() {
        Query query = currentSession().createQuery("select i from Invoice as i ");
        return query.list();
    }
}
