package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private LoteService service;

    @Test
    void calcularStatusVencido() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().minusDays(1));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.VENCIDO,status);
    }

    @Test
    void calcularStatusCritico() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(4));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.CRITICO,status);
    }

    @Test
    void calcularStatusProximoDaValidade() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(7));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.PROXIMO_VENCIMENTO,status);
    }

    @Test
    void calcularStatusNormal() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(8));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.NORMAL,status);
    }
}