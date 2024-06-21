package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ed_sinc.model.Turma;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.pesquisa.TurmaPesquisaProjection;


public interface TurmaRepository extends CustomJpaRepository<Turma, Long>{
	
	List<Turma> findByNomeContaining(String nome);
	Optional<Turma> findByNome(String nome);

	@Query("UPDATE Turma a SET a.status = ?1 WHERE a.id = ?2")
	@Modifying
	void updateStatus(Status status, Long id);
	
	@Query(
		    value = "SELECT DISTINCT u FROM Turma u WHERE u.nome LIKE %:turmaNome% AND (:turmaStatus IS NULL OR u.status = :turmaStatus) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM Turma u WHERE u.nome LIKE %:turmaNome% AND (:turmaStatus IS NULL OR u.status = :turmaStatus)"
		)
		Page<TurmaPesquisaProjection> listarTurmaPesquisaProjetado(
		    @Param("turmaNome") String turmaNome,
		    @Param("turmaStatus") Status turmaStatus,
		    Pageable pageable
		);
	
    @Query("SELECT COALESCE(MAX(t.id), 0) FROM Turma t")
    Long lastId();
	
}