package com.seekcat.reggie.pojo.DTO;

import com.seekcat.reggie.pojo.Dish;
import com.seekcat.reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
