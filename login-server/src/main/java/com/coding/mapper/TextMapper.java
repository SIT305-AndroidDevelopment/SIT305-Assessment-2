package com.coding.mapper;

import com.coding.domain.Text;
import com.coding.domain.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author guanweiming
 */
@Repository
public interface TextMapper extends Mapper<Text>, SelectByIdListMapper<Text,Long> {
}
