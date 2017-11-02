package ggalantsev.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Variant {

    private Long id;
    private String size;
    private BigDecimal price;
    private BigDecimal initialPrice;

}
