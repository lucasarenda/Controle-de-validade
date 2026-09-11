package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import br.com.lucas.controle_validade.service.UserService;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean UserService userService;
    @MockitoBean EstabelecimentoService estabelecimentoService;

    @Test
    void postUsuarioRetorna201() throws Exception {
        when(userService.cadastrarUser(any())).thenReturn(
                new UserResponseDTO(UUID.randomUUID(), "Lucas", "l@e.com", LocalDateTime.now()));
        mvc.perform(post("/usuarios").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Lucas\",\"email\":\"l@e.com\",\"senha\":\"123456\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.nome").value("Lucas"));
    }

    @Test
    void getEstabelecimentosUsaRotaAninhada() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        when(estabelecimentoService.buscaEstabelecimentoPorUsuario(usuarioId)).thenReturn(List.of());
        mvc.perform(get("/usuarios/{id}/estabelecimentos", usuarioId))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(estabelecimentoService).buscaEstabelecimentoPorUsuario(usuarioId);
    }

    @Test
    void patchUsuarioRetorna200ComRecursoAtualizado() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.atualizarUser(eq(id), any())).thenReturn(
                new UserResponseDTO(id, "Lucas Silva", "lucas@email.com", LocalDateTime.now()));
        mvc.perform(patch("/usuarios/{id}", id).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Lucas Silva\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Lucas Silva"));
    }

    @Test
    void beanValidationPreservaCamposInvalidos() throws Exception {
        mvc.perform(post("/usuarios").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"email\":\"invalido\",\"senha\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Dados inválidos"))
                .andExpect(jsonPath("$.path").value("/usuarios"))
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.senha").exists());
        verifyNoInteractions(userService);
    }
}
