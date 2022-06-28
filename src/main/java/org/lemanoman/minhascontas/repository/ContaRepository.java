package org.lemanoman.minhascontas.repository;

import org.lemanoman.minhascontas.model.Conta;
import org.lemanoman.minhascontas.security.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {


}
