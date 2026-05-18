package br.com.senai.cadastroDeCursoBKD.controller;

import br.com.senai.cadastroDeCursoBKD.cursos.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

//Executar o projeto e checar se a documentação foi gerada, acessando:
//http://localhost:8080/swagger-ui/index.html

@RestController
@RequestMapping("cursos")
@Tag(name="Cursos",description="Gerenciamento dos cursos da escola")
@OpenAPIDefinition(tags ={
        @Tag(name = "Cadastrar curso",description = "Cadastrar"),
        @Tag(name = "Listar todos os cursos",description = "Listar todos os cursos"),
        @Tag(name = "Listar curso por ID",description = "Listar por ID"),
        @Tag(name = "Excluir curso",description = "Excluir"),
        @Tag(name = "Atualizar curso",description = "Atualizar"),
        @Tag(name = "Listar todos os períodos",description = "Listar todos os períodos")
})
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar um novo curso")
    @Tag(name="Cadastrar curso", description = "Salva os dados do curso no BD")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Curso já cadastrado", content = @Content)
    })
    public ResponseEntity<DadosDetalhamentoCurso> cadastrarCurso(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DadosCadastroCurso.class),
                            examples = @ExampleObject(
                                    value = """
                {
                  "nome": "Nome do curso",
                  "periodo": "MATUTINO"
                }
                """
                            )
                    )
            )
            @RequestBody @Valid DadosCadastroCurso dados){
        //1. Verificar se o curso existe
        var verificarCurso = cursoRepository.findByNomeAndPeriodoAndAtivo(
                dados.nome(),
                dados.periodo(),
                true
        );

        verificarCurso.ifPresent(curso -> {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Curso já existe."
            );
        });



        //2. Cadastrar o curso
        Curso curso = new Curso(dados);
        cursoRepository.save(curso);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DadosDetalhamentoCurso(curso));
    }

    @GetMapping
    @Operation(summary = "Listar cursos")
    @Tag(name="Listar todos os cursos")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "cursos listados com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class))
                    }),
    })
    public ResponseEntity<Page<DadosListagemCurso>> listarCursos(@PageableDefault(size = 10, sort = {"nome"}) @ParameterObject Pageable paginacao){
        var page = cursoRepository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar curso detalhadamente")
    @Tag(name="Listar curso por ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "curso listado com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class))
                    }),
            @ApiResponse(responseCode = "404", description = "curso não encontrado", content = @Content)
    })
    public ResponseEntity<DadosDetalhamentoCurso> buscarcursoPorId(@PathVariable Long id){
        Curso curso = cursoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não existe"
                ));
        return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Tag(name="Excluir curso")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "curso excluído com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class))
                    }),
            @ApiResponse(responseCode = "404", description = "curso não encontrado.", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity excluircurso(@PathVariable Long id){
        var curso = cursoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "curso não encontrado"));
        curso.excluirCurso();

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar curso")
    @Tag(name="Atualizar curso")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "curso atualizado com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Curso já cadastrado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content)
    })
    public ResponseEntity<DadosDetalhamentoCurso> atualizarcurso(
            @RequestBody @Valid DadosAtualizarCurso dados
    ){
        //1. Verificar se o curso existe
        var curso = cursoRepository.findByIdAndAtivoTrue(dados.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não encontrado"
                ));

        curso.atualizarCurso(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
    }

    @GetMapping("/periodos")
    @Operation(
            summary = "Listar períodos",
            description = "Retorna todos os períodos disponíveis para os cursos"
    )
    @Tag(name = "Listar todos os períodos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Períodos listados com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    example = """
                                                        [
                                                          "MATUTINO",
                                                          "VESPERTINO",
                                                          "NOTURNO..."
                                                        ]
                                                        """
                                            )
                                    )
                            )
                    }
            )
    })
    public ResponseEntity<List<Curso.Periodo>> listarPeriodos() {

        return ResponseEntity.ok(Arrays.asList(Curso.Periodo.values()));
    }

}