package com.coding.mapper;

import com.coding.domain.BrandCollect;
import com.coding.domain.MotorCollect;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface BrandCollectMapper extends Mapper<BrandCollect>, SelectByIdListMapper<BrandCollect,Long> {
}
