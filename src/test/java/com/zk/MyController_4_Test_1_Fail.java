package com.zk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.controller.UserController;
import com.zk.entity.User;
import com.zk.service.UserService;
import org.assertj.core.api.Java6Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// 默认不测这个，因为这是个失败示例，测不过
@Ignore
@RunWith(SpringRunner.class)
// 仅仅使用web layer的测试，controller里的service不会初始化，controller缺依赖，application context启动不起来。除非mock一个service
@WebMvcTest(UserController.class)
public class MyController_4_Test_1_Fail {


	@Autowired
	private MockMvc mvc;

	// 不mock一个service是不可能启动的，因为没加载spring context，所以controller缺service
//	@MockBean
//	private UserService userService;

	@Test
	public void getUserTest() throws Exception {
		// given
		User bob = new User().setName("bob").setId(1);

		// when
		MockHttpServletResponse response = mvc.perform(
				get("/user/1")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		ObjectMapper objectMapper = new ObjectMapper();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(
			objectMapper.writeValueAsString(bob)
		);
	}
	@Test
	public void getUserNotFound() throws Exception {
		// given
		// when
		MockHttpServletResponse response = mvc.perform(
				get("/users/999")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		Java6Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		Java6Assertions.assertThat(response.getContentAsString()).isEmpty();
	}
}
