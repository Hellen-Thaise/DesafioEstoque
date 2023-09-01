package com.desafio.estoque.controller;

import com.desafio.estoque.model.ProdutoModel;
import com.desafio.estoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estoque")

public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @Operation(summary = "Buscar todos os produtos cadastrados", description = "Busca todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Not found - Nenhum produto cadastrado!")
    })
    @GetMapping
    public List<ProdutoModel> listar(){

        return produtoService.listar();
    }

    @Operation(summary = "Buscar um produto pelo ID", description = "Retorna um produto conforme o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Not found - Produto não encontrado")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ProdutoModel> buscarPorId(@PathVariable Long id){

        Optional<ProdutoModel> produtoModel = produtoService.buscarPorId(id);

        if (produtoModel.isPresent()){

            return ResponseEntity.ok(produtoModel.get());

        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @Operation(summary = "Deleta um produto pelo ID", description = "Deleta um produto conforme o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Not found - Produto não encontrado")
    })
    @DeleteMapping(path = "/{id}")
    public void deletarProduto(@PathVariable Long id){

        Optional<ProdutoModel> produto = produtoService.buscarPorId(id);
        if (produto.isPresent()){

            produtoService.deletarProduto(id);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id não encontrado!");
        }
    }
    @Operation(summary = "Cadastrar um novo produto", description = "Cadastra um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Not found - Produto não cadastrado")
    })
    @PostMapping
    public ResponseEntity<ProdutoModel> cadastrar(@RequestBody ProdutoModel produtoModel){
        ProdutoModel novoProduto = produtoService.cadastrar(produtoModel);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }
    @Operation(summary = "Alterar um produto pelo ID", description = "Altera dados de um produto ao buscar o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Not found - Produto não encontrado")
    })
    @PutMapping(path = "/{id}")
        public ProdutoModel alterar(@PathVariable Long id,
                                    @RequestBody ProdutoModel produtoModel){

        Optional<ProdutoModel> produto = produtoService.buscarPorId(id);
        if (produto.isPresent()){

            return produtoService.alterar(id, produtoModel);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id não encontrado!");
        }
    }

}
