package com.example.application.data.repository;

import com.example.application.data.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    Alimento findByNombre(String nombre);
}
