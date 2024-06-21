package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ed_sinc.model.PessoaNeurodivergente;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;


public interface PessoaNeurodivergenteRepository extends CustomJpaRepository<PessoaNeurodivergente, Long>{
	
	List<PessoaNeurodivergente> findByNomeContaining(String nome);
	Optional<PessoaNeurodivergente> findByNome(String nome);

	@Query("UPDATE PessoaNeurodivergente a SET a.status = ?1 WHERE a.id = ?2")
	@Modifying
	void updateStatus(Status status, Long id);
	
	@Query(
		    value = "SELECT DISTINCT u FROM PessoaNeurodivergente u WHERE u.nome LIKE %:pessoaNeurodivergenteNome% AND (:pessoaNeurodivergenteStatus IS NULL OR u.status = :pessoaNeurodivergenteStatus) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM PessoaNeurodivergente u WHERE u.nome LIKE %:pessoaNeurodivergenteNome% AND (:pessoaNeurodivergenteStatus IS NULL OR u.status = :pessoaNeurodivergenteStatus)"
		)
		Page<GenericoIdNomeDescricaoStatusProjection> listarPessoaNeurodivergentePesquisaProjetado(
		    @Param("pessoaNeurodivergenteNome") String pessoaNeurodivergenteNome,
		    @Param("pessoaNeurodivergenteStatus") Status pessoaNeurodivergenteStatus,
		    Pageable pageable
		);
	
}