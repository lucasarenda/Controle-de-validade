package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.EstabelecimentoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstabelecimentoController.class)
@WithMockUser
class EstabelecimentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EstabelecimentoService service;

    @Test
    void deveCadastrarEstabelecimento() throws Exception {

        UUID usuarioId = UUID.randomUUID();

        String json = """
                {
                  "nome": "Mercado",
                  "email": "mercado@email.com",
                  "cnpj": "123",
                  "telefone": "9999",
                  "endereco": "Rua A",
                  "usuarioId": "%s"
                }
                """.formatted(usuarioId);

        mvc.perform(
                        post("/estabelecimentos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Estabelecimento cadastrado com sucesso!!"));

        verify(service)
                .cadastrarEstabelecimento(any(EstabelecimentoRequestDTO.class));
    }

    @Test
    void deveBuscarEstabelecimentosPorUsuario() throws Exception {

        UUID usuarioId = UUID.randomUUID();

        var dto = new EstabelecimentoResponseDTO(
                UUID.randomUUID(),
                "Mercado",
                "m@e.com",
                "123",
                "999",
                "Rua A",
                usuarioId
        );

        when(service.buscaEstabelecimentoPorUsuario(usuarioId))
                .thenReturn(List.of(dto));

        mvc.perform(
                        get("/estabelecimentos/{id}", usuarioId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Mercado"))
                .andExpect(jsonPath("$[0].usuarioId").value(usuarioId.toString()));
    }

    @Test
    void deveRemoverEstabelecimento() throws Exception {

        UUID id = UUID.randomUUID();

        mvc.perform(
                        delete("/estabelecimentos/{id}", id)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Estabelecimento removido com sucesso!!"));

        verify(service).removeEstabelecimento(id);
    }

    @Test
    void deveAtualizarEstabelecimentoParcialmente() throws Exception {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        var response = new EstabelecimentoResponseDTO(
                id, "Mercado Central", "m@e.com", "123", "999", "Rua B", usuarioId);
        when(service.atualizarEstabelecimento(eq(id), any(EstabelecimentoUpdateDTO.class)))
                .thenReturn(response);

        mvc.perform(patch("/estabelecimentos/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mercado Central\",\"endereco\":\"Rua B\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Mercado Central"))
                .andExpect(jsonPath("$.endereco").value("Rua B"))
                .andExpect(jsonPath("$.usuarioId").value(usuarioId.toString()));

        verify(service).atualizarEstabelecimento(eq(id), any(EstabelecimentoUpdateDTO.class));
    }
}
