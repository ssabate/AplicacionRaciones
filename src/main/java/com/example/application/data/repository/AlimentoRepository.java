package com.example.application.data.repository;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    Alimento findByNombre(String nombre);

    @Query("select c from Alimento c " +
            "where lower(c.nombre) like lower(concat('%', :searchTerm, '%'))")
    List<Alimento> search(@Param("searchTerm") String searchTerm);
}
