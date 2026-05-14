package br.com.senai.cadastroDeCursoBKD.cursos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findAllByAtivoTrue(Pageable paginacao);
    Optional<Curso> findByIdAndAtivoTrue(Long id);
    Optional<Curso>findByNomeAndPeriodoAndAtivo(String nome, Curso.Periodo periodo, boolean ativo);

}
