package com.seekcat.reggie.pojo.DTO;

import com.seekcat.reggie.pojo.Setmeal;
import com.seekcat.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
