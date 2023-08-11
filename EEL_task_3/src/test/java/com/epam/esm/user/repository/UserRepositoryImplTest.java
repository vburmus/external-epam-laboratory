package com.epam.esm.user.repository;

import com.epam.esm.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    public static final long ID1 = 1L;
    public static final long ID2 = 2L;
    public static final String NAME1 = "FIRST";
    public static final String NAME2 = "SECOND";
    public static final String NUMBER1 = "112";
    public static final String NUMBER2 = "122";
    private EmbeddedDatabase embeddedDatabase;
    private UserRepositoryImpl userRepository;
    @BeforeEach
    public void init(){
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embeddedDB/task_3.sql")
                .addScript("embeddedDB/insert-some-data.sql")
                .build();
        this.userRepository = new UserRepositoryImpl(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    void getAllUsersAndGetUser() {
        User user1 = new User().setId(ID1).setName(NAME1).setSurname(NAME1).setNumber(NUMBER1);
        User user2 = new User().setId(ID2).setName(NAME2).setSurname(NAME2).setNumber(NUMBER2);
        assertEquals(List.of(user1,user2), userRepository.getAllUsers(1,10));
        assertEquals(user1,userRepository.getUserByID(ID1));
    }

    @Test
    void getUserByID() {
    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}