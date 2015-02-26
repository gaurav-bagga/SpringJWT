package com.green.jwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.green.jwt.model.User;
import com.green.jwt.service.TokenService;
 

/**
 * This class is responsible for testing the {@link LoginController}.
 * 
 * @author gaurav.bagga
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-config.xml","file:src/main/webapp/WEB-INF/mvc-config.xml"})
@WebAppConfiguration
public class LoginControllerTest {
    private MockMvc mockMvc;
    
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private TokenService tokenService;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void itShouldReturnJWTTokenForValidUser() throws Exception{
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
        
        Map<String, Object> params = tokenService.verify(token);
        
        Assert.assertNotNull(token);
        Assert.assertNotNull(params);
        
        Assert.assertEquals("james", params.get("sub"));
    }
    
    @Test
    public void itShouldReturn403ForInValidUser() throws Exception{
    	//given a user with in valid credentials
        User user = new User("james", "pass123", null);
        
        //when he logs into the system
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
        
       //then he should not get a status code unauthorized         
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
        
        //and no token should be returned
        MockHttpServletResponse response = result.getResponse();
        String token = response.getContentAsString();
        
        try{
        	tokenService.verify(token);
        	 Assert.fail();
        }catch(Exception e){
        	
        }
        
    }
}
