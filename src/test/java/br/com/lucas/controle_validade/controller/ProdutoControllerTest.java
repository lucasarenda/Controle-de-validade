package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProdutoService service;

    @Test
    void deveCadastrarProduto() throws Exception {

        UUID estabelecimentoId = UUID.randomUUID();

        String json = """
                {
                  "nome": "Arroz",
                  "descricao": "Branco",
                  "marca": "Marca",
                  "categoria": "Alimento",
                  "estabelecimentoId": "%s"
                }
                """.formatted(estabelecimentoId);

        mvc.perform(
                post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Produto cadastrado com sucesso!!"));

        verify(service).cadastrarProduto(any(ProdutoRequestDTO.class));
    }

    @Test
    void deveBuscarProdutosPorEstabelecimento() throws Exception {
        UUID estabelecimentoId = UUID.randomUUID();
        var dto = new ProdutoResponseDTO(
                UUID.randomUUID(),
                "Arroz",
                "Branco",
                "Marca",
                "Alimento",
                LocalDateTime.now(),
                estabelecimentoId
        );

        when(service.buscaProdutosPorEstabelecimento(estabelecimentoId))
                .thenReturn(List.of(dto));

        mvc.perform(
                get("/produtos/{id}", estabelecimentoId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Arroz"))
                .andExpect(jsonPath("$[0].estabelecimentoId").value(estabelecimentoId.toString()));
    }

    @Test
    void deveRemoverProduto() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(
                delete("/produtos/{id}", id)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Produto removido com sucesso!!"));

        verify(service).removerProduto(id);
    }
}
