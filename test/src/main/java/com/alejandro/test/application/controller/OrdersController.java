package com.alejandro.test.application.controller;

import com.alejandro.test.application.service.OrdersService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    public OrdersService ordersService;

    @ResponseBody
    @GetMapping(value = "/synchronize", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 200, message = "Return status")})
    public String synchronizeOrders() {
        return ordersService.synchronizeOrders();
    }

    @ResponseBody
    @GetMapping(value = "/generate-csv", produces = "text/csv")
    @ApiResponses({@ApiResponse(code = 200, message = "Return status")})
    public ResponseEntity generateCSV(HttpServletResponse response) {
        response.setContentType("text/csv");
        File f = ordersService.generateCSV();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + f.getName())
                .contentLength(f.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new FileSystemResource(f))
                ;

    }
}
