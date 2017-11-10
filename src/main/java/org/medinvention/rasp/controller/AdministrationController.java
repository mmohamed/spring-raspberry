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

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.medinvention.rasp.model.User;
import org.medinvention.rasp.repository.RoleRepository;
import org.medinvention.rasp.service.UserService;
import org.medinvention.rasp.utility.datatable.DataTableCallback;
import org.medinvention.rasp.utility.datatable.DataTableDriver;
import org.medinvention.rasp.utility.datatable.DataTableResponse;
import org.medinvention.rasp.utility.http.exception.NotFoundException;
import org.medinvention.rasp.utility.view.FormUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin")
public class AdministrationController {

    @Autowired
    private DataTableDriver datatableDriver;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("/user")
    public ModelAndView userListView() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("user.list");

        return modelAndView;
    }

    @RequestMapping("/user/data")
    public @ResponseBody DataTableResponse userListAjax(HttpServletRequest request) throws Exception {
        datatableDriver.detach();

        datatableDriver.bind(request, User.class, Arrays.asList(
                new String[] { "id", "firstName", "lastName", "email", "level", "roles", "createdAt", "updatedAt" }));

        return datatableDriver.getData(new DataTableCallback() {
            @Override
            public void callback(DataTableResponse response) {
                for (int i = 0; i < response.getAaData().size(); i++) {

                    String level = response.getAaData().get(i).get(4);

                    if (level != null) {
                        response.getAaData().get(i).set(4,
                                level.substring(0, 1).toUpperCase() + level.substring(1).toLowerCase());
                    }
                }
            }
        });
    }

    @GetMapping("/user/new")
    public ModelAndView createUserView() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("user.new");
        modelAndView.addObject("user", new User());
        modelAndView.addObject("roles", roleRepository.findAll());

        return modelAndView;
    }

    @PostMapping("/user/new")
    public ModelAndView createUserAction(HttpServletRequest request, @Validated(User.ValidationCreate.class) User user,
            BindingResult bindingResult) {
        // validate repeated password
        if (request.getParameter("repeatpassword") == null
                || request.getParameter("repeatpassword").compareTo(user.getPassword()) != 0) {

            bindingResult.addError(new FieldError("user", "password", "Please provide a valid repeated password"));
        }

        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user.new");
            modelAndView.addObject("user", user);
            modelAndView.addObject("roles", roleRepository.findAll());
        }
        else {
            this.userService.save(user);
            modelAndView.setViewName("redirect:/admin/user");
        }
        return modelAndView;
    }

    @GetMapping("/user/edit/{userId}")
    public ModelAndView editUserView(@PathVariable Long userId) throws NotFoundException {

        User user = this.userService.load(userId);

        if (user == null) {
            throw new NotFoundException("Not found user with ID " + userId);
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("user.edit");
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", roleRepository.findAll());

        return modelAndView;
    }

    @PostMapping("/user/edit/{userId}")
    public ModelAndView editUserAction(HttpServletRequest request, @PathVariable Long userId,
            @Validated(User.ValidationUpdate.class) User userView, BindingResult bindingResult) throws Exception {

        User user = this.userService.load(userId);

        if (user == null) {
            throw new NotFoundException("Not found user with ID " + userId);
        }

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user.edit");
            modelAndView.addObject("user", userView);
            modelAndView.addObject("roles", roleRepository.findAll());

            return modelAndView;
        }

        FormUtility.bind(request, userView, user);

        this.userService.update(user);

        modelAndView.setViewName("redirect:/admin/user");

        return modelAndView;
    }
}
