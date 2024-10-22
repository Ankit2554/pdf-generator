package com.example.pdf.Model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceRequest {
    private String seller;
    private String sellerGstin;
    private String sellerAddress;
    private String buyer;
    private String buyerGstin;
    private String buyerAddress;
    private String name;
    private String quantity;
    private double rate;
    private double amount;

    }

