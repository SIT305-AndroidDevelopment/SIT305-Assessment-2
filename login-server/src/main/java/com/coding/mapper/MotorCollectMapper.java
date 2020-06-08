package com.coding.mapper;

import com.coding.domain.MotorCollect;
import com.coding.domain.MotorItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface MotorCollectMapper extends Mapper<MotorCollect>, SelectByIdListMapper<MotorCollect,Long> {
}
