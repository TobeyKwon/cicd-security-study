package com.example.study.damain.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
public class Authority {
    @Id
    @Enumerated(EnumType.STRING)
    private Role role;
}
