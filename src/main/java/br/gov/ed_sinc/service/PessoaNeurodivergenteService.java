package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.PessoaNeurodivergenteNaoEncontradoException;
import br.gov.ed_sinc.filter.PessoaNeurodivergenteFilter;
import br.gov.ed_sinc.model.PessoaNeurodivergente;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;
import br.gov.ed_sinc.projection.pesquisa.PagePessoaNeurodivergentePesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PagePessoaNeurodivergentePesquisaProjectionImpl;
import br.gov.ed_sinc.repository.PessoaNeurodivergenteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PessoaNeurodivergenteService {

	PessoaNeurodivergenteRepository pessoaNeurodivergenteRepository;
	UsuarioService usuarioService;

	public PessoaNeurodivergente buscarOuFalhar(Long id) {
		return pessoaNeurodivergenteRepository.findById(id).orElseThrow(() -> new PessoaNeurodivergenteNaoEncontradoException(id));
	}

	@Transactional
	public void deletarPessoaNeurodivergente(Long id) {
		pessoaNeurodivergenteRepository.deleteById(id);
	}
	
	@Transactional
	public PessoaNeurodivergente adicionarPessoaNeurodivergente(PessoaNeurodivergente pessoaNeurodivergente) {
		pessoaNeurodivergenteRepository.detach(pessoaNeurodivergente);
		
		boolean nomeEmUso = pessoaNeurodivergenteRepository.findByNome(pessoaNeurodivergente.getNome()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(pessoaNeurodivergente));
		if (nomeEmUso) {
			throw new NegocioException(
					String.format("Já existe um grupo social cadastrado com este nome %s", pessoaNeurodivergente.getNome()));
		}
		
		return pessoaNeurodivergenteRepository.save(pessoaNeurodivergente);
	}

	public List<PessoaNeurodivergente> listarPessoaNeurodivergente() {
		return pessoaNeurodivergenteRepository.findAll();
	}

	@Transactional
	public ResponseEntity<Void> arquivarPessoaNeurodivergenteId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaNeurodivergente> pessoaNeurodivergenteArquivado = pessoaNeurodivergenteRepository.findById(id);
		if (pessoaNeurodivergenteArquivado.isPresent()) {
			pessoaNeurodivergenteRepository.updateStatus(Status.Arquivado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	public ResponseEntity<Void> restaurarPessoaNeurodivergenteId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaNeurodivergente> pessoaNeurodivergenteRestaurado = pessoaNeurodivergenteRepository.findById(id);
		if (pessoaNeurodivergenteRestaurado.isPresent()) {
			pessoaNeurodivergenteRepository.updateStatus(Status.Ativo, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> bloquearPessoaNeurodivergenteId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaNeurodivergente> pessoaNeurodivergenteArquivado = pessoaNeurodivergenteRepository.findById(id);
		if (pessoaNeurodivergenteArquivado.isPresent()) {
			pessoaNeurodivergenteRepository.updateStatus(Status.Bloqueado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public PagePessoaNeurodivergentePesquisaProjection listarPessoaNeurodivergentePesquisaProjetado(PessoaNeurodivergenteFilter filtro, Pageable pageable){
		 Page<GenericoIdNomeDescricaoStatusProjection> page = pessoaNeurodivergenteRepository.listarPessoaNeurodivergentePesquisaProjetado(filtro.getNome(), filtro.getStatus(), pageable);
		return new PagePessoaNeurodivergentePesquisaProjectionImpl(page);
	}
	
}