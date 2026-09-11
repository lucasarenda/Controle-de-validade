package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.EstabelecimentoUpdateDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoCnpjEstabelecimentoUnico;
import br.com.lucas.controle_validade.validation.ValidacaoNomeEstabelecimentoUnico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstabelecimentoServiceTest {
    @Mock UserRepository userRepository;
    @Mock EstabelecimentoRepository repository;
    @Mock ValidacaoNomeEstabelecimentoUnico validacaoNome;
    @Mock ValidacaoCnpjEstabelecimentoUnico validacaoCnpj;
    @InjectMocks EstabelecimentoService service;

    @Test
    void cadastraComCamposCorretosENomeNormalizado() {
        UUID usuarioId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getId()).thenReturn(usuarioId);
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        var dto = new EstabelecimentoRequestDTO(" Mercado   Central ", "LOJA@EMAIL.COM", "123",
                "999", "Rua A", usuarioId);
        var resposta = service.cadastrarEstabelecimento(dto);
        assertEquals("Mercado Central", resposta.nome());
        assertEquals("loja@email.com", resposta.email());
        assertEquals("123", resposta.cnpj());
        verify(validacaoNome).validar("Mercado Central");
    }

    @Test
    void paiExistenteSemFilhosRetornaListaVazia() {
        UUID usuarioId = UUID.randomUUID();
        when(userRepository.existsById(usuarioId)).thenReturn(true);
        when(repository.findByUser_Id(usuarioId)).thenReturn(List.of());
        assertTrue(service.buscaEstabelecimentoPorUsuario(usuarioId).isEmpty());
    }

    @Test
    void paiInexistenteRetornaErro() {
        UUID usuarioId = UUID.randomUUID();
        when(userRepository.existsById(usuarioId)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscaEstabelecimentoPorUsuario(usuarioId));
        verify(repository, never()).findByUser_Id(any());
    }

    @Test
    void updateSemAlterarCamposUnicosNaoValidaDuplicidade() {
        UUID id = UUID.randomUUID();
        Estabelecimento estabelecimento = estabelecimento();
        when(repository.findById(id)).thenReturn(Optional.of(estabelecimento));
        service.atualizarEstabelecimento(id,
                new EstabelecimentoUpdateDTO(" mercado ", null, "123", "888", null));
        verifyNoInteractions(validacaoNome, validacaoCnpj);
        assertEquals("888", estabelecimento.getTelefone());
    }

    @Test
    void updateComNovosCamposUnicosValidaDuplicidade() {
        UUID id = UUID.randomUUID();
        Estabelecimento estabelecimento = estabelecimento();
        when(repository.findById(id)).thenReturn(Optional.of(estabelecimento));
        service.atualizarEstabelecimento(id,
                new EstabelecimentoUpdateDTO("Nova Loja", null, "456", null, null));
        verify(validacaoNome).validar("Nova Loja");
        verify(validacaoCnpj).validar("456");
    }

    private Estabelecimento estabelecimento() {
        return new Estabelecimento("Mercado", "m@e.com", "123", "999", "Rua A", mock(User.class));
    }
}
