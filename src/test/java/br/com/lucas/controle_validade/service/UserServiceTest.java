package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEmailUsuarioUnico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private ValidacaoEmailUsuarioUnico validacaoEmailUsuarioUnico;

    @InjectMocks
    private UserService service;

    @Test
    void deveCadastrarUsuario() {
        var dto = new UserRequestDTO("Lucas", " LUCAS@email.com ", "123456");
        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO resultado = service.cadastrarUser(dto);

        assertEquals("Lucas", resultado.nome());
        assertEquals("lucas@email.com", resultado.email());
        verify(validacaoEmailUsuarioUnico).validar(dto);
        verify(repository).save(any(User.class));
    }

    @Test
    void deveBuscarTodosUsuarios() {
        User user = usuario(UUID.randomUUID(), "Lucas");
        when(repository.findAll())
                .thenReturn(List.of(user));

        List<UserResponseDTO> resultado = service.buscaTodosUsers();

        assertEquals(1, resultado.size());
        assertEquals("Lucas", resultado.get(0).nome());
    }

    @Test
    void deveBuscarUsuarioPeloNome() {
        var esperado = new UserResponseDTO(
                UUID.randomUUID(),
                "Lucas",
                "lucas@email.com",
                LocalDateTime.now()
        );
        when(repository.findByNome("Lucas"))
                .thenReturn(esperado);

        assertSame(esperado, service.buscaUsuarioPeloNome("Lucas"));
    }

    @Test
    void deveRemoverUsuario() {
        UUID id = UUID.randomUUID();
        User user = usuario(id, "Lucas");
        when(repository.findById(id))
                .thenReturn(Optional.of(user));

        service.removerUser(id);

        verify(repository).delete(user);
    }

    @Test
    void deveFalharAoRemoverUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.removerUser(id));
        verify(repository, never()).delete(any());
    }

    @Test
    void deveAtualizarSomenteNomeDoUsuario() {
        UUID id = UUID.randomUUID();
        User user = usuario(id, "Lucas");
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserResponseDTO resultado = service.atualizarUser(id, new UserUpdateDTO("Lucas Silva", null));

        assertEquals("Lucas Silva", resultado.nome());
        assertEquals("lucas@email.com", resultado.email());
        verify(repository).save(user);
        verifyNoInteractions(validacaoEmailUsuarioUnico);
    }

    @Test
    void deveAtualizarNomeEEmailDoUsuarioSemAlterarDemaisCampos() {
        UUID id = UUID.randomUUID();
        User user = usuario(id, "Lucas");
        LocalDateTime dataCadastro = user.getDataCadastro();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserResponseDTO resultado = service.atualizarUser(
                id, new UserUpdateDTO("Lucas Silva", " NOVO@EMAIL.COM "));

        assertEquals("Lucas Silva", resultado.nome());
        assertEquals("novo@email.com", resultado.email());
        assertEquals(dataCadastro, resultado.dataCadastro());
        assertEquals("123456", user.getSenha());
        verify(validacaoEmailUsuarioUnico).validar("novo@email.com");
        verify(repository).save(user);
    }

    @Test
    void deveFalharAoAtualizarUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarUser(id, new UserUpdateDTO("Novo nome", null)));
        verify(repository, never()).save(any());
    }

    private User usuario(UUID id, String nome) {
        return new User(
                id,
                nome,
                "lucas@email.com",
                "123456",
                LocalDateTime.now(),
                List.of()
        );
    }
}
