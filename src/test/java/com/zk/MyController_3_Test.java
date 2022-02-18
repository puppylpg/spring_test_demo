package com.zk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.entity.User;
import com.zk.exception.UserNotFound;
import com.zk.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 使用mockmvc就意味着不启动server！！！
// to enable and configure auto-configuration of MockMvc
// 既用了spring，又手动操作MockMvc
// 不启用server，只测试其下的layer，几乎所有的spring组件都会被加载，就差启动server的开销了
@AutoConfigureMockMvc
public class MyController_3_Test {

	@Autowired
	MockMvc mvc;


	/**
	 * 用mock bean而不是test1中的mock，为了能注入spring
	 */
	@MockBean
	UserService userService;




	@Test
	public void getUser() throws Exception {
		// given
		User lily = new User().setName("lily").setId(1);
		given(userService.getUser(1))
				.willReturn(lily);


		// when
		MockHttpServletResponse response = mvc.perform(
				get("/user/1")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		ObjectMapper objectMapper = new ObjectMapper();
		Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
		Assert.assertEquals(response.getContentAsString(), objectMapper.writeValueAsString(lily));
	}

	@Test
	public void getUserNotFound() throws Exception {
		// given
		given(userService.getUser(999))
				.willThrow(new UserNotFound());

		// when
		MockHttpServletResponse response = mvc.perform(
				// get几都是exception，因为user service是mock的，没有注入实际的user repo
				get("/users/1")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(response.getContentAsString()).isEmpty();
	}
}
