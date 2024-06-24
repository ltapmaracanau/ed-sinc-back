package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.PessoaComDeficienciaNaoEncontradoException;
import br.gov.ed_sinc.filter.PessoaComDeficienciaFilter;
import br.gov.ed_sinc.model.PessoaComDeficiencia;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;
import br.gov.ed_sinc.projection.pesquisa.PagePessoaComDeficienciaPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PagePessoaComDeficienciaPesquisaProjectionImpl;
import br.gov.ed_sinc.repository.PessoaComDeficienciaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PessoaComDeficienciaService {

	PessoaComDeficienciaRepository pessoaComDeficienciaRepository;
	UsuarioService usuarioService;

	public PessoaComDeficiencia buscarOuFalhar(Long id) {
		return pessoaComDeficienciaRepository.findById(id).orElseThrow(() -> new PessoaComDeficienciaNaoEncontradoException(id));
	}

	@Transactional
	public void deletarPessoaComDeficiencia(Long id) {
		pessoaComDeficienciaRepository.deleteById(id);
	}
	
	@Transactional
	public PessoaComDeficiencia adicionarPessoaComDeficiencia(PessoaComDeficiencia pessoaComDeficiencia) {
		pessoaComDeficienciaRepository.detach(pessoaComDeficiencia);
		
		boolean nomeEmUso = pessoaComDeficienciaRepository.findByNome(pessoaComDeficiencia.getNome()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(pessoaComDeficiencia));
		if (nomeEmUso) {
			throw new NegocioException(
					String.format("Já existe uma PCD cadastrado com este nome %s", pessoaComDeficiencia.getNome()));
		}
		
		return pessoaComDeficienciaRepository.save(pessoaComDeficiencia);
	}

	public List<PessoaComDeficiencia> listarPessoaComDeficiencia() {
		return pessoaComDeficienciaRepository.findAll();
	}

	@Transactional
	public ResponseEntity<Void> arquivarPessoaComDeficienciaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaComDeficiencia> pessoaComDeficienciaArquivado = pessoaComDeficienciaRepository.findById(id);
		if (pessoaComDeficienciaArquivado.isPresent()) {
			pessoaComDeficienciaRepository.updateStatus(Status.Arquivado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	public ResponseEntity<Void> restaurarPessoaComDeficienciaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaComDeficiencia> pessoaComDeficienciaRestaurado = pessoaComDeficienciaRepository.findById(id);
		if (pessoaComDeficienciaRestaurado.isPresent()) {
			pessoaComDeficienciaRepository.updateStatus(Status.Ativo, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> bloquearPessoaComDeficienciaId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<PessoaComDeficiencia> pessoaComDeficienciaArquivado = pessoaComDeficienciaRepository.findById(id);
		if (pessoaComDeficienciaArquivado.isPresent()) {
			pessoaComDeficienciaRepository.updateStatus(Status.Bloqueado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public PagePessoaComDeficienciaPesquisaProjection listarPessoaComDeficienciaPesquisaProjetado(PessoaComDeficienciaFilter filtro, Pageable pageable){
		 Page<GenericoIdNomeDescricaoStatusProjection> page = pessoaComDeficienciaRepository.listarPessoaComDeficienciaPesquisaProjetado(filtro.getNome(), filtro.getStatus(), pageable);
		return new PagePessoaComDeficienciaPesquisaProjectionImpl(page);
	}
	
}