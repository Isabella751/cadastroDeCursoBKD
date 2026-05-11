package br.com.senai.cadastroDeCursoBKD.cursos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosAtualizarCurso(
        @NotNull
        Long id,

        @Size(min = 3, max = 100)
        String nome,

        Curso.Periodo periodo
) {
}
