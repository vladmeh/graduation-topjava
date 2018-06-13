package com.vladmeh.choosing.web;


import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static com.vladmeh.choosing.testdata.UserTestData.*;
import static com.vladmeh.choosing.utils.TestUtil.userHttpBasic;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractControllerTest {

    private static final String USERS_URL = REST_URL + "/users/";

    @Override
    public void getAll() throws Exception {
        ResultActions resultActions = testGetAll(USERS_URL, ADMIN);
    }

    @Override
    public void getById() throws Exception {
        ResultActions resultActions = testGetById(USERS_URL + USER_ID, ADMIN);

        resultActions
                .andExpect(jsonPath("name", is(USER.getName())))
                .andExpect(jsonPath("email", is(USER.getEmail())));
    }

    @Test
    public void getUserByEmail() throws Exception {
        mockMvc
                .perform(get(USERS_URL + "search/by-email")
                        .param("email", USER.getEmail())
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(USER.getName())))
                .andExpect(jsonPath("email", is(USER.getEmail())));
    }

    @Test
    public void getIsForbidden() throws Exception {
        ResultActions resultActions = testGetIsForbidden(USERS_URL, USER);
    }

    @Override
    public void getIsNotFound() throws Exception {
        ResultActions resultActions = testGetIsNotFound(USERS_URL + 2, ADMIN);
    }

    @Override
    public void create() throws Exception {
        ResultActions resultActions = testCreate(USERS_URL, ADMIN, objectMapper.writeValueAsString(getCreatedUser()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createIsConflict() throws Exception {
        Map<String, Object> created = getCreatedUser();
        created.put("email", "user@yandex.ru");

        ResultActions resultActions = testCreateIsConflict(USERS_URL, ADMIN, objectMapper.writeValueAsString(created));
    }

    @Override
    public void createIsForbidden() throws Exception {
        ResultActions resultActions = testCreateIsForbidden(USERS_URL, USER, objectMapper.writeValueAsString(getCreatedUser()));
    }

    @Override
    public void update() throws Exception {
        ResultActions resultActions = testUpdate(USERS_URL + USER_ID, ADMIN, objectMapper.writeValueAsString(getUpdateUser()));
    }

    @Test
    public void updateIsConflict() throws Exception {
        Map<String, Object> update = getUpdateUser();
        update.put("email", "admin@gmail.com");

        ResultActions resultActions = testUpdateIsConflict(USERS_URL + USER_ID, ADMIN, objectMapper.writeValueAsString(update));

    }

    @Override
    public void updateIsForbidden() throws Exception {
        ResultActions resultActions = testUpdateIsForbidden(USERS_URL + USER_ID, USER, objectMapper.writeValueAsString(getUpdateUser()));
    }

    @Override
    public void deleted() throws Exception {
        ResultActions resultActions = testDelete(USERS_URL + USER_ID, ADMIN);
    }

    @Override
    public void deletedIsForbidden() throws Exception {
        ResultActions resultActions = testDeleteIsForbidden(USERS_URL + USER_ID, USER);
    }
}
