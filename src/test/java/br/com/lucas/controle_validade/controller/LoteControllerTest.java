package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoteController.class)
@WithMockUser
class LoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private LoteService loteService;

    @Test
    void confirmaCadastroDoLote() throws Exception {
        UUID produtoId = UUID.randomUUID();
        UUID loteId = UUID.randomUUID();

        String json = """
                {
                  "numeroLote": "LOTE-001",
                  "quantidade": 50,
                  "custoUnitario": 7.90,
                  "dataEntrada": "2026-08-24",
                  "dataValidade": "2026-09-30",
                  "endereco": "Corredor A - Prateleira 2",
                  "produtoId": "%s"
                }
                """.formatted(produtoId);

        LoteResponseDTO responseDTO = new LoteResponseDTO(
                loteId,
                "LOTE-001",
                50,
                new BigDecimal("7.90"),
                LocalDate.of(2026, 8, 24),
                LocalDate.of(2026, 9, 30),
                "Corredor A - Prateleira 2",
                produtoId,
                30L,
                StatusValidade.NORMAL
        );

        when(loteService.cadastrarLote(any(LoteRequestDTO.class)))
                .thenReturn(responseDTO);

        mvc.perform(
                        post("/lotes")
                                .with(csrf())
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loteId.toString()))
                .andExpect(jsonPath("$.numeroLote").value("LOTE-001"))
                .andExpect(jsonPath("$.quantidade").value(50))
                .andExpect(jsonPath("$.custoUnitario").value(7.90))
                .andExpect(jsonPath("$.endereco").value("Corredor A - Prateleira 2"));

        verify(loteService)
                .cadastrarLote(any(LoteRequestDTO.class));
    }

    @Test
    void buscaLotePorId() throws Exception {
        UUID produtoId = UUID.randomUUID();
        UUID loteId = UUID.randomUUID();

        LoteResponseDTO responseDTO = new LoteResponseDTO(
                loteId,
                "LOTE-001",
                50,
                new BigDecimal("7.90"),
                LocalDate.of(2026, 8, 24),
                LocalDate.of(2026, 9, 30),
                "Corredor A - Prateleira 2",
                produtoId,
                30L,
                StatusValidade.NORMAL
        );

        when(loteService.buscaLotePorId(loteId))
                .thenReturn(responseDTO);

        mvc.perform(
                        get("/lotes/{id}", loteId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loteId.toString()))
                .andExpect(jsonPath("$.numeroLote").value("LOTE-001"))
                .andExpect(jsonPath("$.quantidade").value(50));

        verify(loteService).buscaLotePorId(loteId);
    }

    @Test
    void buscaLotePorProduto() throws Exception {
        List<LoteResponseDTO> lotes = new ArrayList<>();

        UUID produtoId = UUID.randomUUID();
        UUID loteId1 = UUID.randomUUID();
        UUID loteId2 = UUID.randomUUID();

        LoteResponseDTO responseDTO1 = new LoteResponseDTO(
                loteId1,
                "LOTE-001",
                50,
                new BigDecimal("7.90"),
                LocalDate.of(2026, 8, 24),
                LocalDate.of(2026, 9, 30),
                "Corredor A - Prateleira 2",
                produtoId,
                30L,
                StatusValidade.NORMAL
        );

        LoteResponseDTO responseDTO2 = new LoteResponseDTO(
                loteId2,
                "LOTE-002",
                50,
                new BigDecimal("7.90"),
                LocalDate.of(2026, 8, 24),
                LocalDate.of(2026, 10, 30),
                "Corredor A - Prateleira 2",
                produtoId,
                30L,
                StatusValidade.NORMAL
        );

        lotes.add(responseDTO1);
        lotes.add(responseDTO2);

        when(loteService.buscaLotesPorProduto(produtoId))
                .thenReturn(lotes);

        mvc.perform(
                        get("/lotes/produto/{produtoId}", produtoId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroLote").value("LOTE-001"))
                .andExpect(jsonPath("$[1].numeroLote").value("LOTE-002"));

        verify(loteService).buscaLotesPorProduto(produtoId);
    }

    @Test
    void removeLote() throws Exception {
        UUID loteId = UUID.randomUUID();

        doNothing()
                .when(loteService)
                .removerLote(loteId);

        mvc.perform(
                        delete("/lotes/{id}", loteId)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Lote removido com sucesso!!"));

        verify(loteService).removerLote(loteId);
    }
}
