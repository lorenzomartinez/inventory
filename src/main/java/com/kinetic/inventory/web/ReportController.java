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
package com.kinetic.inventory.web;

import com.kinetic.inventory.dao.InvoiceDao;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/report")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/invoice/{invoice_id}")
    public String invoice(HttpSession session, HttpServletResponse response, @PathVariable Long invoice_id) {
        ServletContext context = session.getServletContext();
        String reportPath = context.getRealPath("/WEB-INF/reports") + "/";
        File reportFile = new File(reportPath + "Invoice.jasper");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUBREPORT_DIR", reportPath);
        parameters.put("INVOICE_ID", invoice_id);

        JasperPrint jasperPrint = null;

        Connection conn = null;
        try {
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObjectFromFile(reportFile.getPath());

            conn = dataSource.getConnection();

            jasperPrint = JasperFillManager.fillReport(jasperReport,
                    parameters, conn);
        } catch (JRException | SQLException e) {
            log.error("Can't create report", e);
            response.setContentType("text/html");
            try {
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Kinetic</title>");
                out.println("</head>");

                out.println("<body bgcolor=\"white\">");

                out
                        .println("<span class=\"bnew\">JasperReports encountered this error :</span>");
                out.println("<pre>");

                e.printStackTrace(out);

                out.println("</pre>");

                out.println("</body>");
                out.println("</html>");
            } catch (Exception ex) {
            }

            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
        if (jasperPrint != null) {
            byte[] pdf = null;
            try {
                pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                response.setContentType("application/pdf");
                response.setContentLength(pdf.length);
                response.setHeader("Content-disposition",
                        "attachment; filename=\"Invoice" + invoice_id + ".pdf\"");
            } catch (JRException ex) {
                log.error("cannot create pdf", ex);
            }

            ServletOutputStream out;
            try {
                out = response.getOutputStream();
                out.write(pdf);
            } catch (IOException e) {
                log.error("cannot write report to stream", e);
            }
        } else {
            response.setContentType("text/html");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Inventory</title>");
                out.println("</head>");

                out.println("<body bgcolor=\"white\">");

                out.println("<span class=\"bold\">Empty response.</span>");

                out.println("</body>");
                out.println("</html>");
            } catch (IOException e) {
                log.error("couldn't write", e);
            }
        }
        return null;
    }
}
