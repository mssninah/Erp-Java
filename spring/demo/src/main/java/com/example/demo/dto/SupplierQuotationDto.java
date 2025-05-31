package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class SupplierQuotationDto {
    
    String company = "Ninah-company";
    String status ="Draft";
    LocalDate date;
    List<QuotationItemDTO> item;
}
