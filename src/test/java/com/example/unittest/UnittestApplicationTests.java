package com.example.unittest;

import com.example.unittest.controller.UserController;
import com.example.unittest.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
class UnittestApplicationTests {
	@Autowired
	private UserController userController;
	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private User user() {
		return new User();
	}
	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}
	@Test
	public void add () throws Exception {
		this.mockMvc.perform((RequestBuilder) post("/test/add")
						.contentType(MediaType.APPLICATION_JSON)
						.contentType(MediaType.valueOf(objectMapper.writeValueAsString(user()))))
				.andExpect(status().isOk());
	}
	public User addAndReturn() throws Exception {
		MvcResult result = this.mockMvc.perform((RequestBuilder) post("/test/adduser")
						.contentType(MediaType.APPLICATION_JSON)
						.contentType(MediaType.valueOf(objectMapper.writeValueAsString(user()))))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		return objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
	}

	@Test
	public void createUser() throws Exception {
		User user = addAndReturn();
		assertThat(user.getId()).isNotZero().isNotNull();
	}

	@Test
	public void getAll() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/user/getlistusers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size())
				.isGreaterThan(0).isNotNull();
	}

	@Test
	public void getByid() throws Exception {
		User user = addAndReturn();
		MvcResult result = this.mockMvc.perform(get("/getuser/{id}" + user.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), User.class).getId())
				.isNotNull();
	}

	@Test
	public void updateById() throws Exception {
		User user = addAndReturn();
		User userupdate = user();
		userupdate.setNome("Maria");
		MvcResult result = this.mockMvc.perform(put("/updateuser/{id}" + user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userupdate)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), User.class).getNome())
				.isEqualTo("Maria");
	}

	@Test
	public void deleteById() throws Exception {
		User user = addAndReturn();
		this.mockMvc.perform(delete("/delete" + user.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertThat(userController.getUserById(user.getId())).isNotNull();
	}

}
