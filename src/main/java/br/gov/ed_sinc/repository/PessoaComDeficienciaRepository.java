package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ed_sinc.model.PessoaComDeficiencia;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;


public interface PessoaComDeficienciaRepository extends CustomJpaRepository<PessoaComDeficiencia, Long>{
	
	List<PessoaComDeficiencia> findByNomeContaining(String nome);
	Optional<PessoaComDeficiencia> findByNome(String nome);

	@Query("UPDATE PessoaComDeficiencia a SET a.status = ?1 WHERE a.id = ?2")
	@Modifying
	void updateStatus(Status status, Long id);
	
	@Query(
		    value = "SELECT DISTINCT u FROM PessoaComDeficiencia u WHERE u.nome LIKE %:pessoaComDeficienciaNome% AND (:pessoaComDeficienciaStatus IS NULL OR u.status = :pessoaComDeficienciaStatus) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM PessoaComDeficiencia u WHERE u.nome LIKE %:pessoaComDeficienciaNome% AND (:pessoaComDeficienciaStatus IS NULL OR u.status = :pessoaComDeficienciaStatus)"
		)
		Page<GenericoIdNomeDescricaoStatusProjection> listarPessoaComDeficienciaPesquisaProjetado(
		    @Param("pessoaComDeficienciaNome") String pessoaComDeficienciaNome,
		    @Param("pessoaComDeficienciaStatus") Status pessoaComDeficienciaStatus,
		    Pageable pageable
		);
	
}