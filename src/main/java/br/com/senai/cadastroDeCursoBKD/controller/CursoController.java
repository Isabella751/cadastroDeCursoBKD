package br.com.senai.cadastroDeCursoBKD.controller;

import br.com.senai.cadastroDeCursoBKD.cursos.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @GetMapping
    public Page<DadosListagemCurso> listarCursos(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);
    }

    @PostMapping
    @Transactional
    public void cadastrarCurso(@RequestBody @Valid DadosCadastroCurso dados){
        repository.save(new Curso(dados));
    }

    @PutMapping
    @Transactional
    public void atualizarCurso(@RequestBody @Valid DadosAtualizarCurso dados){
        var curso = repository.getReferenceById(dados.id());
        curso.atualizarCurso(dados);
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public void deletarCurso(@PathVariable Long id){
        var curso = repository.getReferenceById(id);
        curso.excluirCurso();
    }

    @GetMapping("/{id}")
    public DadosDetalhamentoCurso detalharCurso(@PathVariable Long id){
        Curso curso = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não existe"
                ));
        return new DadosDetalhamentoCurso(curso);
    }

    @GetMapping("/periodos")
    public List<Curso.Periodo> listarPeriodos() {

        return Arrays.asList(Curso.Periodo.values());
    }
}


























package br.com.senai.cadastroDeCursoBKD.controller;

import br.com.senai.cadastroDeCursoBKD.cursos.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
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

//Executar o projeto e checar se a documentação foi gerada, acessando:
//http://localhost:8080/swagger-ui/index.html

