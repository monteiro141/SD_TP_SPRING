package com.example.noticiasquentinhas.entities;

import javax.persistence.Transient;

public class UserRegistrationDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String profilePic;
    private String profilePicPath;



    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String name, String lastName,String email, String password, String role) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserRegistrationDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.profilePic = user.getProfilePic();
        this.profilePicPath = null;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Transient
    public String getProfilePicPath(){
        if(profilePic == null || id == null)
            return "/profile-pics/0/defaultPic.png";
        return "/profile-pics/" + id + "/" + profilePic;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}