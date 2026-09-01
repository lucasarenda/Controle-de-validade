package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoNomeEstabelecimentoUnico;
import br.com.lucas.controle_validade.validation.ValidacaoUsuarioPossuiEstabelecimentos;
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
class EstabelecimentoServiceTest {
    @Mock
    private UserRepository repositoryUser;

    @Mock
    private EstabelecimentoRepository repositoryEstabelecimento;

    @Mock
    private ValidacaoUsuarioPossuiEstabelecimentos validacaoUsuarioPossuiEstabelecimentos;

    @Mock
    private ValidacaoNomeEstabelecimentoUnico validacaoNomeEstabelecimentoUnico;

    @InjectMocks
    private EstabelecimentoService service;

    @Test
    void deveCadastrarEstabelecimento() {
        UUID usuarioId = UUID.randomUUID();
        User user = new User(
                usuarioId,
                "Lucas",
                "lucas@email.com",
                "123456",
                LocalDateTime.now(),
                List.of()
        );
        var dto = new EstabelecimentoRequestDTO(
                "Mercado",
                "mercado@email.com",
                "123",
                "9999",
                "Rua A",
                usuarioId
        );
        when(repositoryUser.findById(usuarioId))
                .thenReturn(Optional.of(user));
        when(repositoryEstabelecimento.save(any(Estabelecimento.class)))
                .thenAnswer(i -> i.getArgument(0));

        var resultado = service.cadastrarEstabelecimento(dto);

        assertEquals("Mercado", resultado.nome());
        assertEquals(usuarioId, resultado.usuarioId());
        verify(validacaoNomeEstabelecimentoUnico).validar(dto);
    }

    @Test
    void deveBuscarEstabelecimentosPorUsuario() {
        UUID usuarioId = UUID.randomUUID();
        User user = new User(
                usuarioId,
                "Lucas",
                "a@b.com",
                "123456",
                LocalDateTime.now(),
                List.of()
        );
        var estabelecimento = new Estabelecimento(
                UUID.randomUUID(),
                "Mercado",
                "m@e.com",
                "123",
                "999",
                "Rua A",
                user,
                List.of()
        );
        when(repositoryEstabelecimento.findByUser_Id(usuarioId))
                .thenReturn(List.of(estabelecimento));

        var resultado = service.buscaEstabelecimentoPorUsuario(usuarioId);

        assertEquals(1, resultado.size());
        assertEquals("Mercado", resultado.get(0).nome());
        verify(validacaoUsuarioPossuiEstabelecimentos).validar(List.of(estabelecimento));
    }

    @Test
    void deveRemoverEstabelecimento() {
        UUID id = UUID.randomUUID();
        var estabelecimento = new Estabelecimento();
        when(repositoryEstabelecimento.findById(id))
                .thenReturn(Optional.of(estabelecimento));

        service.removeEstabelecimento(id);

        verify(repositoryEstabelecimento).delete(estabelecimento);
    }

    @Test
    void deveFalharQuandoUsuarioDoCadastroNaoExiste() {
        var dto = new EstabelecimentoRequestDTO(
                "Mercado",
                "m@e.com",
                "123",
                "999",
                "Rua A",
                UUID.randomUUID()
        );
        when(repositoryUser.findById(dto.usuarioId()))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.cadastrarEstabelecimento(dto));
        verify(repositoryEstabelecimento, never()).save(any());
    }
}
