package hello.mapper;import hello.entity.User;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import org.apache.ibatis.annotations.Select;import org.springframework.stereotype.Component;@Mapper@Componentpublic interface UserMapper {    @Select("SELECT * FROM user WHERE id = #{id}")    User getUserById(@Param("id") Integer id);}