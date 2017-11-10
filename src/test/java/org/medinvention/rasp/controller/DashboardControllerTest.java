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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.medinvention.rasp.BaseControllerTestUnit;
import org.springframework.http.MediaType;
import org.hamcrest.Matchers;

public class DashboardControllerTest extends BaseControllerTestUnit {

    @Test
    public void testRedirect() throws Exception {
        this.mvc.perform(get("/").accept(MediaType.TEXT_PLAIN)).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testExceptions() throws Exception {
        this.mvc.perform(get("/admin/user/edit/9999")).andExpect(status().isNotFound());
        this.mvc.perform(get("/admin/user/data")).andExpect(status().isBadRequest());
    }

    @Test
    public void testDashboard() throws Exception {
        this.mvc.perform(get("/dashboard").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("MedInvention !")));
    }
}
