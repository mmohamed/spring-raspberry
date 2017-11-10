/*******************************************************************************
 * Copyright 2017 Marouan MOHAMED
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.medinvention.rasp.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.medinvention.rasp.BaseControllerTestUnit;
import org.medinvention.rasp.model.Role;
import org.medinvention.rasp.model.User;
import org.medinvention.rasp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;

public class AdministrationControllerTest extends BaseControllerTestUnit {

    @Autowired
    private UserService userService;

    @Test
    public void testUserListView() throws Exception {
        this.mvc.perform(get("/admin/user").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("list of application users")));
    }

    @Test
    public void testUserListData() throws Exception {
        this.mvc.perform(get("/admin/user/data?sEcho=1&iDisplayStart=0&iDisplayLength=10").header("X-Requested-With",
                "XMLHttpRequest")).andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.sEcho", is("1"))).andExpect(jsonPath("$.iTotalRecords", is(3)))
                .andExpect(jsonPath("$.iTotalRecords", is(3))).andExpect(jsonPath("$.aaData", hasSize(3)));
    }

    @Test
    public void testCreateUserView() throws Exception {
        this.mvc.perform(get("/admin/user/new")).andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8)).andExpect(view().name("user.new"))
                .andExpect(model().attribute("user", hasProperty("firstName", nullValue())))
                .andExpect(model().attribute("user", hasProperty("lastName", nullValue())))
                .andExpect(model().attribute("user", hasProperty("email", nullValue())))
                .andExpect(model().attribute("user", hasProperty("password", nullValue())))
                .andExpect(model().attribute("user", hasProperty("roles", nullValue())))
                .andExpect(xpath("//input[@name='repeatpassword']").exists());
    }

    @Test
    public void testCreateUserValidation() throws Exception {
        this.mvc.perform(post("/admin/user/new").with(csrf())).andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8)).andExpect(view().name("user.new"))
                .andExpect((model().attributeHasFieldErrors("user", "firstName")))
                .andExpect((model().attributeHasFieldErrors("user", "lastName")))
                .andExpect((model().attributeHasFieldErrors("user", "email")))
                .andExpect((model().attributeHasFieldErrors("user", "roles")))
                .andExpect((model().attributeHasFieldErrors("user", "password")));

        this.mvc.perform(
                post("/admin/user/new").param("password", "abcdef").param("repeatpassword", "abcdeg").with(csrf()))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML_UTF8))
                .andExpect(view().name("user.new"))
                .andExpect(xpath("//li[contains(text(),'Please provide a valid repeated password')]").exists());

        this.mvc.perform(post("/admin/user/new").param("email", "admin@medinvention.ext.io").with(csrf()))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML_UTF8))
                .andExpect(view().name("user.new")).andExpect((model().attributeHasErrors("user")));
    }

    @Test
    public void testCreateUserCreateAction() throws Exception {
        MockHttpServletRequestBuilder newUserRequest = post("/admin/user/new").with(csrf())
                .param("firstName", "Firstname").param("lastName", "LastName")
                .param("email", "test@medinvention.ext.io").param("password", "abcdef")
                .param("repeatpassword", "abcdef").param("roles", new String[] { "1", "3" });

        this.mvc.perform(newUserRequest).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/user"));

        Assert.isTrue(this.userService.findUserByEmail("test@medinvention.ext.io") != null, "Created user");
    }

    @Test
    public void testUpdateUserView() throws Exception {
        User user = this.userService.findUserByEmail("user@medinvention.ext.io");

        Assert.isTrue(user != null, "Not founded user");

        Role role = new Role(2, "USER");

        this.mvc.perform(get("/admin/user/edit/" + user.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8)).andExpect(view().name("user.edit"))
                .andExpect(model().attribute("user", hasProperty("firstName", is("Philippe"))))
                .andExpect(model().attribute("user", hasProperty("lastName", is("DUBOIS"))))
                .andExpect(model().attribute("user", hasProperty("roles", org.hamcrest.Matchers.hasItem(role))))
                .andExpect(model().attribute("user", hasProperty("roles", hasSize(1))))
                .andExpect(xpath("//input[@name='password']").doesNotExist());
    }

    @Test
    public void testUpdateUserValidation() throws Exception {
        User user = this.userService.findUserByEmail("user@medinvention.ext.io");

        Assert.isTrue(user != null, "Founded user");

        MockHttpServletRequestBuilder newUserRequest = post("/admin/user/edit/" + user.getId()).with(csrf())
                .param("firstName", "").param("lastName", "").param("email", "");

        this.mvc.perform(newUserRequest).andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML_UTF8))
                .andExpect(view().name("user.edit")).andExpect((model().attributeHasFieldErrors("user", "firstName")))
                .andExpect((model().attributeHasFieldErrors("user", "lastName")))
                .andExpect((model().attributeHasFieldErrors("user", "roles")))
                .andExpect((model().attributeHasFieldErrors("user", "email")));

        newUserRequest = post("/admin/user/edit/" + user.getId()).with(csrf()).param("email",
                "guest@medinvention.ext.io");

        this.mvc.perform(newUserRequest).andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML_UTF8))
                .andExpect(view().name("user.edit")).andExpect((model().attributeHasErrors("user")));
    }

    @Test
    public void testUpdateUserAction() throws Exception {
        User user = this.userService.findUserByEmail("user@medinvention.ext.io");

        Assert.isTrue(user != null, "Founded user");

        MockHttpServletRequestBuilder newUserRequest = post("/admin/user/edit/" + user.getId()).with(csrf())
                .param("firstName", "New Philippe").param("lastName", "New DUBOIS")
                .param("email", "new.user@medinvention.ext.io").param("roles", new String[] { "2" });

        this.mvc.perform(newUserRequest).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/user"));

        Assert.isNull(this.userService.findUserByEmail("user@medinvention.ext.io"), "Old user not found");
        Assert.isTrue(this.userService.findUserByEmail("new.user@medinvention.ext.io") != null, "New user found");
    }
}
