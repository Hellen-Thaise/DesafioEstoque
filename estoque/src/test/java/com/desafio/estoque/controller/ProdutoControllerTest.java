package com.desafio.estoque.controller;

import com.desafio.estoque.model.ProdutoModel;
import com.desafio.estoque.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProdutoController produtoController;

    @MockBean
    ProdutoService produtoService;

    @BeforeEach
    public void setup() {

        RestAssuredMockMvc.standaloneSetup(this.produtoController);
    }

    @Test
    public void deveRetornarOk_AoBuscarProduto(){
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(1L);
        produtoModel.setNome("Sapatilha");
        produtoModel.setDescricao("n° 38");
        produtoModel.setPreco(50.00);
        produtoModel.setQtdArmazenada(3);

        Mockito.when(this.produtoService.buscarPorId(1L)).thenReturn(Optional.of(produtoModel));

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/estoque/{id}", 1L)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void deveRetornarNaoEncontrado_AoBuscarProdutoInexistente(){

        Mockito.when(this.produtoService.buscarPorId(1L))
                .thenReturn(Optional.empty());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/estoque/{id}", 1L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deveRetornarOkAoCadastrarNovoProduto() throws Exception {
        ProdutoModel produto = new ProdutoModel();
        produto.setId(1L);
        produto.setNome("Sapatilha");
        produto.setDescricao("n° 38");
        produto.setPreco(100.00);
        produto.setQtdArmazenada(2);

        Mockito.when(produtoService.cadastrar(any())).thenReturn(produto);

        mockMvc.perform(MockMvcRequestBuilders.post("/estoque")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Sapatilha"))
                .andExpect(jsonPath("$.descricao").value("n° 38"))
                .andExpect(jsonPath("$.preco").value(100.00))
                .andExpect(jsonPath("$.qtdArmazenada").value(2));

    }

    @Test
    public void deveDeletarProduto_AoChamarDeletarProduto() throws Exception {

        ProdutoModel produto = new ProdutoModel();
        produto.setId(1L);
        produto.setNome("Sapatilha");
        produto.setDescricao("n° 38");
        produto.setPreco(100.00);
        produto.setQtdArmazenada(2);

        Mockito.when(produtoService.cadastrar(any())).thenReturn(produto);

        Mockito.when(this.produtoService.buscarPorId(1L)).thenReturn(Optional.of(produto));

        Mockito.doNothing().when(produtoService).deletarProduto(produto.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/estoque/{id}",produto.getId() ))
                .andExpect(status().isOk());

        Mockito.verify(produtoService, Mockito.times(1)).deletarProduto(produto.getId());

    }


//    @Test
//    public void deveAlterarProduto_AoChamarAlterarProduto() throws Exception {
//
//        ProdutoModel produto = new ProdutoModel();
//        produto.setId(1L);
//        produto.setNome("Sapatilha");
//        produto.setDescricao("n° 38");
//        produto.setPreco(100.00);
//        produto.setQtdArmazenada(2);
//
//        Mockito.when(produtoService.cadastrar(any())).thenReturn(produto);
//
//        Mockito.when(this.produtoService.buscarPorId(1L)).thenReturn(Optional.of(produto));
//
//        Mockito.when(produtoService.alterar(produto.getId(), produto)).thenReturn(produto);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/estoque/{id}",produto.getId() ))
//                .andExpect(status().isOk());
//
//        Mockito.verify(produtoService, Mockito.times(1)).alterar(produto.getId(), produto);
//
//    }



}