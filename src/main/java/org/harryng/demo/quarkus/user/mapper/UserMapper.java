package org.harryng.demo.quarkus.user.mapper;

import org.harryng.demo.quarkus.base.mapper.BaseStatedMapper;
import org.harryng.demo.quarkus.user.dto.User;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface UserMapper extends BaseStatedMapper<User, UserImpl> {

}
