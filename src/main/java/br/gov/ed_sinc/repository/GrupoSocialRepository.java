package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ed_sinc.model.GrupoSocial;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;


public interface GrupoSocialRepository extends CustomJpaRepository<GrupoSocial, Long>{
	
	List<GrupoSocial> findByNomeContaining(String nome);
	Optional<GrupoSocial> findByNome(String nome);

	@Query("UPDATE GrupoSocial a SET a.status = ?1 WHERE a.id = ?2")
	@Modifying
	void updateStatus(Status status, Long id);
	
	@Query(
		    value = "SELECT DISTINCT u FROM GrupoSocial u WHERE u.nome LIKE %:grupoSocialNome% AND (:grupoSocialStatus IS NULL OR u.status = :grupoSocialStatus) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM GrupoSocial u WHERE u.nome LIKE %:grupoSocialNome% AND (:grupoSocialStatus IS NULL OR u.status = :grupoSocialStatus)"
		)
		Page<GenericoIdNomeDescricaoStatusProjection> listarGrupoSocialPesquisaProjetado(
		    @Param("grupoSocialNome") String grupoSocialNome,
		    @Param("grupoSocialStatus") Status grupoSocialStatus,
		    Pageable pageable
		);
	
}
