package com.seekcat.reggie.entity.DTO;

import com.seekcat.reggie.entity.Setmeal;
import com.seekcat.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
