package com.example.cloudass2.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="type")
public class Type {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Column(name="name",unique = true)
    private String name;

    public Type() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
