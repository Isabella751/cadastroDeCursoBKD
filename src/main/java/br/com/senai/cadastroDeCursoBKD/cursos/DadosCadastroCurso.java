package br.com.senai.cadastroDeCursoBKD.cursos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroCurso(

        @NotBlank
        @Size(min = 3, max = 100)
        @Column(unique = true)
        String nome,

        @NotNull
        Curso.Periodo periodo
) {

}
