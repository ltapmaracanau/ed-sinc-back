package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.TurmaNaoEncontradoException;
import br.gov.ed_sinc.filter.TurmaFilter;
import br.gov.ed_sinc.model.Turma;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.pesquisa.PageTurmaPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.TurmaPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PageTurmaPesquisaProjectionImpl;
import br.gov.ed_sinc.repository.TurmaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TurmaService {

	TurmaRepository turmaRepository;
	UsuarioService usuarioService;

	public Turma buscarOuFalhar(Long id) {
		return turmaRepository.findById(id).orElseThrow(() -> new TurmaNaoEncontradoException(id));
	}

	@Transactional
	public void deletarTurma(Long id) {
		turmaRepository.deleteById(id);
	}
	
    @Transactional
    public Turma adicionarTurma(Turma turma) {
        turmaRepository.detach(turma);
        System.out.println(turma.getUsuarios());

        Long ultimoId = turmaRepository.lastId();

        turma.setNome("Turma-" + (ultimoId + 1));

        boolean nomeEmUso = turmaRepository.findByNome(turma.getNome()).stream()
                .anyMatch(turmaExistente -> !turmaExistente.equals(turma));
        if (nomeEmUso) {
            throw new NegocioException(
                    String.format("Já existe uma Turma com este nome %s", turma.getNome()));
        }
        
        return turmaRepository.save(turma);
    }

	public List<Turma> listarTurma() {
		return turmaRepository.findAll();
	}

	@Transactional
	public ResponseEntity<Void> arquivarTurmaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Turma> turmaArquivado = turmaRepository.findById(id);
		if (turmaArquivado.isPresent()) {
			turmaRepository.updateStatus(Status.Arquivado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	public ResponseEntity<Void> restaurarTurmaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Turma> turmaRestaurado = turmaRepository.findById(id);
		if (turmaRestaurado.isPresent()) {
			turmaRepository.updateStatus(Status.Ativo, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> bloquearTurmaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Turma> turmaArquivado = turmaRepository.findById(id);
		if (turmaArquivado.isPresent()) {
			turmaRepository.updateStatus(Status.Bloqueado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public PageTurmaPesquisaProjection listarTurmaPesquisaProjetado(TurmaFilter filtro, Pageable pageable){
		 Page<TurmaPesquisaProjection> page = turmaRepository.listarTurmaPesquisaProjetado(filtro.getNome(), filtro.getStatus(), pageable);
		return new PageTurmaPesquisaProjectionImpl(page);
	}
	
}