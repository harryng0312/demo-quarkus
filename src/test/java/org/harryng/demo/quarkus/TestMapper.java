package org.harryng.demo.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;

@QuarkusTest
public class TestMapper {
    static Logger logger = LoggerFactory.getLogger(TestMapper.class);

    @Inject
    protected UserMapper mapper;

    @Test
    public void testUserMapper(){
        logger.info("===== mapper =====");
//        mapper = Mappers.getMapper(UserMapper.class);
        var userEntity = new UserImpl();
        var now = LocalDateTime.now();
        userEntity.setId(3L);
        userEntity.setCreatedDate(now);
        userEntity.setModifiedDate(now);
        userEntity.setStatus("1");
        userEntity.setUsername("username 3");
        userEntity.setPassword("passwd2");
        userEntity.setScreenName("");
        userEntity.setDob(now.toLocalDate());
        userEntity.setPasswdEncryptedMethod("plain");

        var userDto = mapper.mapEntityToDto(userEntity);
        logger.info("dto user.username:" + userDto.getUsername());
    }
}
