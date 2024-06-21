package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.PoloNaoEncontradoException;
import br.gov.ed_sinc.filter.PoloFilter;
import br.gov.ed_sinc.model.Polo;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;
import br.gov.ed_sinc.projection.pesquisa.PagePoloPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PagePoloPesquisaProjectionImpl;
import br.gov.ed_sinc.repository.PoloRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PoloService {

	PoloRepository poloRepository;
	UsuarioService usuarioService;

	public Polo buscarOuFalhar(Long id) {
		return poloRepository.findById(id).orElseThrow(() -> new PoloNaoEncontradoException(id));
	}

	@Transactional
	public void deletarPolo(Long id) {
		poloRepository.deleteById(id);
	}
	
	@Transactional
	public Polo adicionarPolo(Polo polo) {
		poloRepository.detach(polo);
		
		boolean nomeEmUso = poloRepository.findByNome(polo.getNome()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(polo));
		if (nomeEmUso) {
			throw new NegocioException(
					String.format("Já existe um grupo social cadastrado com este nome %s", polo.getNome()));
		}
		
		return poloRepository.save(polo);
	}

	public List<Polo> listarPolo() {
		return poloRepository.findAll();
	}

	@Transactional
	public ResponseEntity<Void> arquivarPoloId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Polo> poloArquivado = poloRepository.findById(id);
		if (poloArquivado.isPresent()) {
			poloRepository.updateStatus(Status.Arquivado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	public ResponseEntity<Void> restaurarPoloId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Polo> poloRestaurado = poloRepository.findById(id);
		if (poloRestaurado.isPresent()) {
			poloRepository.updateStatus(Status.Ativo, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> bloquearPoloId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<Polo> poloArquivado = poloRepository.findById(id);
		if (poloArquivado.isPresent()) {
			poloRepository.updateStatus(Status.Bloqueado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public PagePoloPesquisaProjection listarPoloPesquisaProjetado(PoloFilter filtro, Pageable pageable){
		 Page<GenericoIdNomeDescricaoStatusProjection> page = poloRepository.listarPoloPesquisaProjetado(filtro.getNome(), filtro.getStatus(), pageable);
		return new PagePoloPesquisaProjectionImpl(page);
	}
	
}