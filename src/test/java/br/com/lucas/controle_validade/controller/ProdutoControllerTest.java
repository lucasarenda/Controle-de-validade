package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@WithMockUser
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
                        .with(csrf())
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
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Produto removido com sucesso!!"));

        verify(service).removerProduto(id);
    }

    @Test
    void deveAtualizarProdutoParcialmente() throws Exception {
        UUID id = UUID.randomUUID();
        UUID estabelecimentoId = UUID.randomUUID();
        var response = new ProdutoResponseDTO(id, "Arroz Integral", "Integral", "Marca",
                "Alimento", LocalDateTime.now(), estabelecimentoId);
        when(service.atualizarProduto(eq(id), any(ProdutoUpdateDTO.class))).thenReturn(response);

        mvc.perform(patch("/produtos/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Arroz Integral\",\"descricao\":\"Integral\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Arroz Integral"))
                .andExpect(jsonPath("$.descricao").value("Integral"))
                .andExpect(jsonPath("$.estabelecimentoId").value(estabelecimentoId.toString()));

        verify(service).atualizarProduto(eq(id), any(ProdutoUpdateDTO.class));
    }
}
