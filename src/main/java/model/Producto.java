package model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Producto implements Serializable {
    // Getters y setters
    private String title;
    private String description;
    private int stock;
    private double price;

}

