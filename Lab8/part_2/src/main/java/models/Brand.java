package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand implements Serializable {
    private int id;
    private int manufactureId;
    private String name;
    private int price;
}