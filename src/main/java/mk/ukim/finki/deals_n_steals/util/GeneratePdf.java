package mk.ukim.finki.deals_n_steals.util;

import com.itextpdf.text.*;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.var;
import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.UserService;

import java.io.*;

public class GeneratePdf {
    private static final Logger logger = LoggerFactory.getLogger(GeneratePdf.class);


    private final OrderService orderService;

    public GeneratePdf(OrderService orderService) {
        this.orderService = orderService;
    }

    public ByteArrayInputStream orderReport(Order order) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();


        try {

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{1, 3, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Category", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Price", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            var productList = order.getProducts();

            PdfPCell cell;

            for ( Product product : productList) {

                cell = new PdfPCell(new Phrase(String.valueOf(product.getId())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(product.getName()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(product.getCategory().getName()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(product.getPrice())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }
            table.addCell("");
            table.addCell("");

            cell = new PdfPCell(new Phrase("Total: "));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(order.getTotal())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            Paragraph p = new Paragraph("Order for " + order.getName());
            p.setAlignment(Paragraph.ALIGN_CENTER);
            p.setSpacingAfter(5);

            Paragraph p1 = new Paragraph(String.format("Order number: %s \nName: %s Surname: %s \nAddress: %s Number: %s\n",
                    order.getOrderNumber().toString(),
                    order.getName(),
                    order.getSurname(),
                    order.getAddress(),
                    order.getPhoneNumber()));
            p1.setSpacingAfter(5);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(p);
            document.add(p1);
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }
        order.setPdf(out.toByteArray());
        this.orderService.save(order);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
