package com.pomodoro.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.lang.Enum.valueOf;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "USER")
@Transactional
public class User implements UserDetails {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "userObject")
    private List<Pomodoro> pomodoros;

    @NotBlank(message = "Name is mandatory")
    private String username;

    private String firstName, lastName;
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @JsonIgnore
    @Pattern(regexp = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,40})"
            , message = "Password must be between 8 to 40 characters long, must contain at least one digit, one upper case character and one lower case character ")

    private String password;

    @JsonIgnore
    private String token;
    @JsonIgnore
    private Boolean accountExpired;
    @JsonIgnore
    private Boolean locked;
    @JsonIgnore
    private Boolean credentialsExpired;
    @JsonIgnore
    private Boolean enabled;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Settings settings;
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private
    Set<Group> groups;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "ownerObject")
    private List<Group> ownedGroups;

    public void setUsername(String userName) {
        this.username = userName;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    @JsonIgnore
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Pomodoro> getPomodoros() {
        return pomodoros;
    }

    public void setPomodoros(List<Pomodoro> pomodoros) {
        this.pomodoros = pomodoros;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
