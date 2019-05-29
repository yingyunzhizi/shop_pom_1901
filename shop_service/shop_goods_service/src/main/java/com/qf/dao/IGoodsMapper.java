package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IGoodsMapper extends BaseMapper<Goods> {

    List<Goods> getGoodsList(@Param("i") int i,@Param("pageSize") int pageSize);

}
