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
package org.medinvention.rasp.utility.validation;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.medinvention.rasp.BaseTestUnit;
import org.medinvention.rasp.model.Role;
import org.medinvention.rasp.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidatorTest extends BaseTestUnit {

    @Autowired
    private Validator validator;

    @Test
    public void testUniqueEmailValidator() {
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role());

        User user = new User();

        user.setEmail("admin@medinvention.ext.io");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setRoles(roles);

        Set<ConstraintViolation<User>> violations = validator.validate(user, User.ValidationCreate.class,
                User.ValidationUpdate.class);
        assertEquals(1, violations.size());
    }

    @Test
    public void testUniqueEmailValidatorOnEdition() {
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role());

        User user = new User();

        user.setId(1L);
        user.setEmail("admin@medinvention.ext.io");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setRoles(roles);

        Set<ConstraintViolation<User>> violations = validator.validate(user, User.ValidationCreate.class,
                User.ValidationUpdate.class);
        assertEquals(0, violations.size());
    }
}
