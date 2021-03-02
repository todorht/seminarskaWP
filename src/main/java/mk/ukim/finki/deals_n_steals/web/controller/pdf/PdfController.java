package mk.ukim.finki.deals_n_steals.web.controller.pdf;

import lombok.var;
import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.util.GeneratePdf;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
public class PdfController {

    private final OrderService orderService;

    public PdfController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/pdf/{number}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> pdfOrder(@PathVariable Long number){

        Order order =  orderService.findByOrderNumber(number);

        ByteArrayInputStream bis = new ByteArrayInputStream(order.getPdf());


        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order" + order.getOrderNumber() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }
}
