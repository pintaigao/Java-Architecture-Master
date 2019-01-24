package com.bjpowernode.dao;

import com.bjpowernode.entity.question;
import com.bjpowernode.entity.questionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface questionMapper {
    int countByExample(questionExample example);

    int deleteByExample(questionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(question record);

    int insertSelective(question record);

    List<question> selectByExampleWithRowbounds(questionExample example, RowBounds rowBounds);

    List<question> selectByExample(questionExample example);

    question selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") question record, @Param("example") questionExample example);

    int updateByExample(@Param("record") question record, @Param("example") questionExample example);

    int updateByPrimaryKeySelective(question record);

    int updateByPrimaryKey(question record);
}