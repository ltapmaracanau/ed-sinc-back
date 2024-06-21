package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.GrupoSocialNaoEncontradoException;
import br.gov.ed_sinc.filter.GrupoSocialFilter;
import br.gov.ed_sinc.model.GrupoSocial;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;
import br.gov.ed_sinc.projection.pesquisa.PageGrupoSocialPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PageGrupoSocialPesquisaProjectionImpl;
import br.gov.ed_sinc.repository.GrupoSocialRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GrupoSocialService {

	GrupoSocialRepository grupoSocialRepository;
	UsuarioService usuarioService;

	public GrupoSocial buscarOuFalhar(Long id) {
		return grupoSocialRepository.findById(id).orElseThrow(() -> new GrupoSocialNaoEncontradoException(id));
	}

	@Transactional
	public void deletarGrupoSocial(Long id) {
		grupoSocialRepository.deleteById(id);
	}
	
	@Transactional
	public GrupoSocial adicionarGrupoSocial(GrupoSocial grupoSocial) {
		grupoSocialRepository.detach(grupoSocial);
		
		boolean nomeEmUso = grupoSocialRepository.findByNome(grupoSocial.getNome()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(grupoSocial));
		if (nomeEmUso) {
			throw new NegocioException(
					String.format("Já existe um grupo social cadastrado com este nome %s", grupoSocial.getNome()));
		}
		
		return grupoSocialRepository.save(grupoSocial);
	}

	public List<GrupoSocial> listarGrupoSocial() {
		return grupoSocialRepository.findAll();
	}

	@Transactional
	public ResponseEntity<Void> arquivarGrupoSocialId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<GrupoSocial> grupoSocialArquivado = grupoSocialRepository.findById(id);
		if (grupoSocialArquivado.isPresent()) {
			grupoSocialRepository.updateStatus(Status.Arquivado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	public ResponseEntity<Void> restaurarGrupoSocialId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<GrupoSocial> grupoSocialRestaurado = grupoSocialRepository.findById(id);
		if (grupoSocialRestaurado.isPresent()) {
			grupoSocialRepository.updateStatus(Status.Ativo, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> bloquearGrupoSocialId(String bearerToken, Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		Optional<GrupoSocial> grupoSocialArquivado = grupoSocialRepository.findById(id);
		if (grupoSocialArquivado.isPresent()) {
			grupoSocialRepository.updateStatus(Status.Bloqueado, id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public PageGrupoSocialPesquisaProjection listarGrupoSocialPesquisaProjetado(GrupoSocialFilter filtro, Pageable pageable){
		 Page<GenericoIdNomeDescricaoStatusProjection> page = grupoSocialRepository.listarGrupoSocialPesquisaProjetado(filtro.getNome(), filtro.getStatus(), pageable);
		return new PageGrupoSocialPesquisaProjectionImpl(page);
	}
	
}
