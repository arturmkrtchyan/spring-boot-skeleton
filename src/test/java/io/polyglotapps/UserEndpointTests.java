package io.polyglotapps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PolyglotApps.class)
public class UserEndpointTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy filterChain;


	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(filterChain).build();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void createNewUser() throws Exception {

		final String PAYLOAD = "{\"full_name\":\"Artur Mkrtchyan\",\"credentials\":{\"email\":\"mkrtchyan.artur@gmail.com\",\"password\":\"qwerty\"}}";

		mvc.perform(post("/v1/users").
				content(PAYLOAD).
				contentType(MediaType.APPLICATION_JSON).
				accept(MediaType.APPLICATION_JSON)).
				andExpect(status().isCreated()).
				andDo(print());
	}


}
