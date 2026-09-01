package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.service.UserService;
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
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService service;

    @Test
    void deveCadastrarUsuario() throws Exception {

        String json = """
                {
                  "nome": "Lucas",
                  "email": "lucas@email.com",
                  "senha": "123456"
                }
                """;

        mvc.perform(
                post("/users/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário cadastrado com sucesso!!"));

        verify(service).cadastrarUser(any(UserRequestDTO.class));
    }

    @Test
    void deveBuscarTodosUsuarios() throws Exception {
        var dto = new UserResponseDTO(
                UUID.randomUUID(),
                "Lucas",
                "lucas@email.com",
                LocalDateTime.now()
        );
        when(service.buscaTodosUsers())
                .thenReturn(List.of(dto));

        mvc.perform(
                get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Lucas"))
                .andExpect(jsonPath("$[0].email").value("lucas@email.com"));
    }

    @Test
    void deveBuscarUsuarioPeloNome() throws Exception {
        var dto = new UserResponseDTO(
                UUID.randomUUID(),
                "Lucas",
                "lucas@email.com",
                LocalDateTime.now()
        );

        when(service.buscaUsuarioPeloNome("Lucas"))
                .thenReturn(dto);

        mvc.perform(get("/users/{nome}", "Lucas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lucas"));
    }

    @Test
    void deveRemoverUsuario() throws Exception {

        UUID id = UUID.randomUUID();

        mvc.perform(
                delete("/users/{id}", id)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário removido com sucesso!!"));

        verify(service).removerUser(id);
    }
}
