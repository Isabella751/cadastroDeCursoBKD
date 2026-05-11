package br.com.senai.cadastroDeCursoBKD.cursos;

public record DadosDetalhamentoCurso(
        Long id,
        String nome,
        Curso.Periodo periodo
) {
    public DadosDetalhamentoCurso(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getPeriodo());
    }
}
