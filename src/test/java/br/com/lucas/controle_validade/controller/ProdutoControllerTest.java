package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.service.LoteService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@WithMockUser
class ProdutoControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean ProdutoService produtoService;
    @MockitoBean LoteService loteService;

    @Test
    void getLotesUsaRotaAninhada() throws Exception {
        UUID id = UUID.randomUUID();
        when(loteService.buscaLotesPorProduto(id)).thenReturn(List.of());
        mvc.perform(get("/produtos/{id}/lotes", id))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(loteService).buscaLotesPorProduto(id);
    }

    @Test
    void deleteRetorna204SemCorpo() throws Exception {
        UUID id = UUID.randomUUID();
        mvc.perform(delete("/produtos/{id}", id).with(csrf()))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        verify(produtoService).removerProduto(id);
    }

    @Test
    void patchUsaRotaDoProprioRecurso() throws Exception {
        UUID id = UUID.randomUUID();
        UUID estabelecimentoId = UUID.randomUUID();
        when(produtoService.atualizarProduto(eq(id), any())).thenReturn(new ProdutoResponseDTO(
                id, "Arroz Integral", "Integral", "M", "A", LocalDateTime.now(), estabelecimentoId));
        mvc.perform(patch("/produtos/{id}", id).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Arroz Integral\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Arroz Integral"));
    }

    @Test
    void recursoNaoEncontradoTemFormatoPadronizado() throws Exception {
        UUID id = UUID.randomUUID();
        when(produtoService.buscarPorId(id)).thenThrow(new RecursoNaoEncontradoException("Produto não encontrado"));
        mvc.perform(get("/produtos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Produto não encontrado"))
                .andExpect(jsonPath("$.path").value("/produtos/" + id))
                .andExpect(jsonPath("$.campos").isMap());
    }
}
