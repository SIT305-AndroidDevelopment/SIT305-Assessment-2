package com.coding.mapper;

import com.coding.domain.MotorCollect;
import com.coding.domain.MotorFoot;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface MotorFootMapper extends Mapper<MotorFoot>, SelectByIdListMapper<MotorFoot,Long> {
}
