package com.seekcat.reggie.entity.DTO;

import com.seekcat.reggie.entity.Dish;
import com.seekcat.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
