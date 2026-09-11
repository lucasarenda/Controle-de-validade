package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEmailUsuarioUnico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repository;
    @Mock ValidacaoEmailUsuarioUnico validacaoEmail;
    @InjectMocks UserService service;

    @Test
    void cadastraUsuarioComEmailNormalizado() {
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resposta = service.cadastrarUser(new UserRequestDTO(" Lucas ", " LUCAS@EMAIL.COM ", "123456"));
        assertEquals("Lucas", resposta.nome());
        assertEquals("lucas@email.com", resposta.email());
        verify(validacaoEmail).validar("lucas@email.com");
    }

    @Test
    void atualizacaoSemAlterarEmailNaoConsultaUnicidade() {
        UUID id = UUID.randomUUID();
        User user = new User("Lucas", "lucas@email.com", "123456");
        when(repository.findById(id)).thenReturn(Optional.of(user));
        var resposta = service.atualizarUser(id, new UserUpdateDTO("Lucas Silva", " LUCAS@EMAIL.COM "));
        assertEquals("Lucas Silva", resposta.nome());
        verifyNoInteractions(validacaoEmail);
    }

    @Test
    void atualizacaoComNovoEmailConsultaUnicidade() {
        UUID id = UUID.randomUUID();
        User user = new User("Lucas", "lucas@email.com", "123456");
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.atualizarUser(id, new UserUpdateDTO(null, "novo@email.com"));
        verify(validacaoEmail).validar("novo@email.com");
        assertEquals("novo@email.com", user.getEmail());
    }

    @Test
    void buscaPorNomeConverteEntidadeEmDto() {
        User user = new User("Lucas", "lucas@email.com", "123456");
        when(repository.findByNomeIgnoreCase("Lucas")).thenReturn(Optional.of(user));
        assertEquals("Lucas", service.buscaUsuarioPeloNome(" Lucas ").nome());
    }

    @Test
    void falhaQuandoUsuarioNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscaUsuarioPorId(id));
    }
}
