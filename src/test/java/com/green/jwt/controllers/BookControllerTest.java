package com.green.jwt.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.green.jwt.model.User;
import com.green.jwt.security.SecurityConstant;
import com.green.jwt.security.SecurityUrlFilter;
 

/**
 * This class is responsible for testing the {@link BookController}.
 * 
 * @author gaurav.bagga
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-config.xml","file:src/main/webapp/WEB-INF/mvc-config.xml"})
@WebAppConfiguration
public class BookControllerTest {
    private MockMvc mockMvc;
    
    @Autowired private WebApplicationContext webApplicationContext;
    
    
    @Before
    public void setUp() throws ServletException {
    	ServletContext context = Mockito.mock(ServletContext.class);
        Mockito.when(context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).thenReturn(webApplicationContext);
    	SecurityUrlFilter securityUrlFilter = new SecurityUrlFilter();
    	securityUrlFilter.init(new MockFilterConfig(context));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(securityUrlFilter, "/*").build();
    }
    
    @Test
    public void itShouldFetchBookDataWithValidToken() throws Exception{
    	//given a valid token
    	String token = getToken();
        
    	//when a request is made to fetch a book by id
    	mockMvc.perform(get("/book/{id}",1l).header(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER, token))
    	
    	//then proper json data should be returned
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.id", is(1)))
    	.andExpect(jsonPath("$.isbn", is("978-3-16-148410-0")))
    	.andExpect(jsonPath("$.title", is("Java")))
    	.andExpect(jsonPath("$.author", is("James Gosling")))
    	.andExpect(jsonPath("$.cost", is(1000)))
        .andDo(print())
        .andReturn();
        
    }
    
    @Test
    public void itShouldNotFetchBookDataWithNoToken() throws Exception{
    	//given a call with no token data
    	//when a request is made to fetch a book by id
    	mockMvc.perform(get("/book/{id}",1l))
    	
    	//then unauthorized status code should be returned
    	.andExpect(status().isUnauthorized())
        .andDo(print())
        .andReturn();
        
    }

	private String getToken() throws Exception, IOException,
			UnsupportedEncodingException {
            
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
           
		//given a user with valid credentials
        User user = new User("james", "pass", null);
        
        //when he logs into the system
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        
        //he should get a valid JWT token
        MockHttpServletResponse response = result.getResponse();
        String token = response.getContentAsString();
		return token;
	}
    
   
}
