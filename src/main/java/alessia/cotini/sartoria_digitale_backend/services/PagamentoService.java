package alessia.cotini.sartoria_digitale_backend.services;

import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Ordine;
import alessia.cotini.sartoria_digitale_backend.entities.Pagamento;
import alessia.cotini.sartoria_digitale_backend.enums.StatoPagamento;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistraAccontoRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistraSaldoRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento creaVuoto(Ordine ordine) {
        Pagamento pagamento = new Pagamento();
        pagamento.setOrdine(ordine);
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento trovaPerOrdine(UUID ordineId) {
        return pagamentoRepository.findByOrdineId(ordineId)
                .orElseThrow(() -> new NotFoundException("Pagamento non trovato per l'ordine " + ordineId));
    }

    public Pagamento registraAcconto(UUID ordineId, RegistraAccontoRequest request) {
        Pagamento pagamento = trovaPerOrdine(ordineId);
        pagamento.setAccontoImporto(request.importo());
        pagamento.setAccontoData(request.data());
        pagamento.setAccontoMetodo(request.metodo());
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento registraSaldo(UUID ordineId, RegistraSaldoRequest request) {
        Pagamento pagamento = trovaPerOrdine(ordineId);
        pagamento.setSaldoImporto(request.importo());
        pagamento.setSaldoData(request.data());
        pagamento.setSaldoMetodo(request.metodo());
        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> trovaTutti() {
        return pagamentoRepository.findAll();
    }

    public StatoPagamento statoDi(Pagamento pagamento) {
        if (pagamento.getSaldoImporto() != null) return StatoPagamento.SALDATO;
        if (pagamento.getAccontoImporto() != null) return StatoPagamento.ACCONTO_VERSATO;
        return StatoPagamento.NON_PAGATO;
    }
}
