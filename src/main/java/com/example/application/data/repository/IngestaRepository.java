package com.example.application.data.repository;

import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngestaRepository extends JpaRepository<Ingesta, Integer> {

    @Query("select c from Ingesta c " +
            "where c.date = :searchDate " +
            "and c.comida = :searchTipo")
    List<Ingesta> findIngestasByDateTipoComida(@Param("searchDate") LocalDate fecha, @Param("searchTipo") TipoComida tipoComida);
//    https://stackoverflow.com/questions/9432034/jpql-query-annotation-with-limit-and-offset
    @Query("select c from Ingesta c " +
            "where c.date = :searchDate " +
            "and c.comida = :searchTipo "+
            "limit :offset, :lim")
    List<Ingesta> fetchIngestas(@Param("offset")int offset, @Param("lim")int limit, @Param("searchDate") LocalDate fecha, @Param("searchTipo") TipoComida tipoComida);
}
