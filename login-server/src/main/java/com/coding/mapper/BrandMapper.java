package com.coding.mapper;

import com.coding.domain.Brand;
import com.coding.domain.MotorCollect;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand,Long> {
}
