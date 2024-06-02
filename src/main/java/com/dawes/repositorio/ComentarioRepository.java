package com.dawes.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.ComentarioVO;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioVO, Integer> {

}
