package com.coding.mapper;

import com.coding.domain.MotorItem;
import com.coding.domain.Text;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface MotorItemMapper extends Mapper<MotorItem>, SelectByIdListMapper<MotorItem,Long> {
}
