package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.exception.custom.DataLoteInvalidaException;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.service.LoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoteController.class)
@WithMockUser
class LoteControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean LoteService loteService;

    @Test
    void postLoteRetorna201() throws Exception {
        UUID produtoId = UUID.randomUUID();
        when(loteService.cadastrarLote(any())).thenReturn(new LoteResponseDTO(
                UUID.randomUUID(), "L1", 1, BigDecimal.ONE, LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 10, 1), "A", produtoId, 20, StatusValidade.NORMAL));
        String json = """
                {"numeroLote":"L1","quantidade":1,"custoUnitario":1,
                "dataEntrada":"2026-09-01","dataValidade":"2026-10-01",
                "endereco":"A","produtoId":"%s"}
                """.formatted(produtoId);
        mvc.perform(post("/lotes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.numeroLote").value("L1"));
    }

    @Test
    void patchLoteRetorna200ComRecursoAtualizado() throws Exception {
        UUID id = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        when(loteService.atualizarLote(eq(id), any())).thenReturn(new LoteResponseDTO(
                id, "L2", 2, BigDecimal.ONE, LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 10, 1), "B", produtoId, 20, StatusValidade.NORMAL));
        mvc.perform(patch("/lotes/{id}", id).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroLote\":\"L2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.numeroLote").value("L2"));
    }

    @Test
    void regraDeDatasTemFormatoPadronizado() throws Exception {
        UUID id = UUID.randomUUID();
        when(loteService.atualizarLote(eq(id), any())).thenThrow(
                new DataLoteInvalidaException("Data de validade não pode ser anterior à data de entrada"));
        mvc.perform(patch("/lotes/{id}", id).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataValidade\":\"2020-01-01\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.path").value("/lotes/" + id))
                .andExpect(jsonPath("$.mensagem").value(
                        "Data de validade não pode ser anterior à data de entrada"));
    }
}
