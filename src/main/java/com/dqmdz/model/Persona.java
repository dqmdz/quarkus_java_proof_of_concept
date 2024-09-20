package com.dqmdz.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Persona extends PanacheEntity {

    public String nombre;
    public String apellido;
    public LocalDate fechaNacimiento;

    public LocalDateTime created;
    public LocalDateTime updated;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = LocalDateTime.now();
    }

}