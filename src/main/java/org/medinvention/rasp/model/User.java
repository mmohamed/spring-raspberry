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
package org.medinvention.rasp.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.medinvention.rasp.utility.validation.UniqueEmailConstraint;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
@UniqueEmailConstraint(message = "Please provide another email (exist)", groups = { User.ValidationCreate.class,
        User.ValidationUpdate.class })
public class User implements UserDetails {

    private static final long serialVersionUID = -4243221153319178473L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid Email", groups = { ValidationCreate.class, ValidationUpdate.class })
    @NotEmpty(message = "Please provide an email", groups = { ValidationCreate.class, ValidationUpdate.class })
    private String email;

    @Column(name = "password", nullable = false)
    @Length(min = 5, message = "Your password must have at least 5 characters", groups = ValidationCreate.class)
    @NotEmpty(message = "Please provide your password", groups = ValidationCreate.class)
    private String password;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "Please provide your first name", groups = { ValidationCreate.class, ValidationUpdate.class })
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Please provide your last name", groups = { ValidationCreate.class, ValidationUpdate.class })
    private String lastName;

    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @NotNull(message = "Please select one or many roles", groups = { ValidationCreate.class, ValidationUpdate.class })
    @Size(min = 1, message = "Please select one or many roles", groups = { ValidationCreate.class,
            ValidationUpdate.class })
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.level != Level.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.level != Level.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.level != Level.EXPIRED && this.level != Level.BLOCKED && this.level != Level.DELETED;
    }

    @PrePersist
    public void onPersist() {
        this.setCreatedAt(new Date());
    }

    @PreUpdate
    public void onMerge() {
        this.setUpdatedAt(new Date());
    }

    public String toString() {
        return this.firstName + ' ' + this.lastName;
    }

    public interface ValidationCreate {
    }

    public interface ValidationUpdate {
    }
}
