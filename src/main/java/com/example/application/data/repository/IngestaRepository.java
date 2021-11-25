package com.example.application.data.repository;

import com.example.application.data.entity.Ingesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngestaRepository extends JpaRepository<Ingesta, Integer> {
}
