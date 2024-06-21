package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ed_sinc.model.Polo;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;


public interface PoloRepository extends CustomJpaRepository<Polo, Long>{
	
	List<Polo> findByNomeContaining(String nome);
	Optional<Polo> findByNome(String nome);

	@Query("UPDATE Polo a SET a.status = ?1 WHERE a.id = ?2")
	@Modifying
	void updateStatus(Status status, Long id);
	
	@Query(
		    value = "SELECT DISTINCT u FROM Polo u WHERE u.nome LIKE %:poloNome% AND (:poloStatus IS NULL OR u.status = :poloStatus) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM Polo u WHERE u.nome LIKE %:poloNome% AND (:poloStatus IS NULL OR u.status = :poloStatus)"
		)
		Page<GenericoIdNomeDescricaoStatusProjection> listarPoloPesquisaProjetado(
		    @Param("poloNome") String poloNome,
		    @Param("poloStatus") Status poloStatus,
		    Pageable pageable
		);
	
}