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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.medinvention.rasp.model.User;
import org.medinvention.rasp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmailConstraint uniqueEmailConstraint) {

    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext cxt) {
        if (user.getEmail() == null) {
            return true;
        }

        User daoUser = this.userRepository.findByEmail(user.getEmail());

        return daoUser == null || daoUser.getId() == user.getId();
    }
}
