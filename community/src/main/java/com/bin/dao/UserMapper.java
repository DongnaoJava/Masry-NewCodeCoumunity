package com.bin.dao;

import com.bin.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    User selectUserById(Integer id);
    User selectUserByName(String username);
    User selectUserByEmail(String email);
    int insertUser(User user);

    @Update("update user set status = #{status} where id = #{id}")
    int updateStatus(Integer id,Integer status);

    @Update("update user set header_url = #{headerUrl} where id = #{id}")
    int updateHeaderUrl(Integer id,String headerUrl);

    @Update("update user set password = #{password} where id = #{id}")
    int updatePassword(Integer id,String password);
}
