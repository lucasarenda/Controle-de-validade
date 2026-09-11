package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import br.com.lucas.controle_validade.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstabelecimentoController.class)
@WithMockUser
class EstabelecimentoControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean EstabelecimentoService estabelecimentoService;
    @MockitoBean ProdutoService produtoService;

    @Test
    void getProdutosUsaRotaAninhada() throws Exception {
        UUID id = UUID.randomUUID();
        when(produtoService.buscaProdutosPorEstabelecimento(id)).thenReturn(List.of());
        mvc.perform(get("/estabelecimentos/{id}/produtos", id))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(produtoService).buscaProdutosPorEstabelecimento(id);
    }

    @Test
    void patchEstabelecimentoRetorna200ComRecursoAtualizado() throws Exception {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        when(estabelecimentoService.atualizarEstabelecimento(eq(id), any())).thenReturn(
                new EstabelecimentoResponseDTO(id, "Mercado Central", "m@e.com", "123",
                        "999", "Rua A", usuarioId));
        mvc.perform(patch("/estabelecimentos/{id}", id).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mercado Central\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Mercado Central"));
    }
}
