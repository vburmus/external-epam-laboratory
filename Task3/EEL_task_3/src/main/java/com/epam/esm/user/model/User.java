package com.epam.esm.user.model;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User {
    private Long id;
    private String name;
    private String surname;
    private String number;

    public User(){}

    public User(Long id, String name, String surname, String number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public User setNumber(String number) {
        this.number = number;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().append(id, user.id).append(name, user.name).append(surname, user.surname).append(number, user.number).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(surname).append(number).toHashCode();
    }
}
