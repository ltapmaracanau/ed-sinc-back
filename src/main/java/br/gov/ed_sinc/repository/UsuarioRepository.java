package br.gov.ed_sinc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.pesquisa.UsuarioPesquisaProjection;
import br.gov.ed_sinc.projection.relatorio.UsuarioRelatorioProjection;


@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{

	List<Usuario> findByNomeContaining(String nome);
	
	Optional<Usuario> findByNome(String nome);
	
	Page<Usuario> findByNomeContaining(String nome, Pageable pageable);
	
	Optional<Usuario> findByEmail(String email);
	
	@Query("UPDATE Usuario u SET u.failedAttempt = ?1 WHERE u.email = ?2")
	@Modifying
	void updateFailedAttempt(Integer failedAttempt, String email);
	
	@Query("UPDATE Usuario u SET u.accountNonLocked = ?1 WHERE u.email = ?2")
	@Modifying
	void updateAccountNonLocked(Boolean accountNonLocked, String email);
	
	@Query("UPDATE Usuario u SET u.status = ?1 WHERE u.email = ?2")
	@Modifying
	void updateStatus(Status status, String email);
	
	Optional <Usuario> findByResetSenhaToken(String token);
	
	List<Usuario> findByAccountNonLocked(Boolean accountNonLocked);
	
	
	@Query("SELECT DISTINCT u " +
		       "FROM Usuario u ")
		List<UsuarioRelatorioProjection> listarUsuariosRelatorioProjetado();
	
	@Query(
		    value = "SELECT DISTINCT u FROM Usuario u WHERE u.nome LIKE %:usuarioNome% AND u.email LIKE %:usuarioEmail% AND (:usuarioStatus IS NULL OR u.status = :usuarioStatus) AND (:usuarioCategoria IS NULL OR :usuarioCategoria MEMBER OF u.categorias) AND (:usuarioExportado IS NULL OR u.exportado = :usuarioExportado) ORDER BY u.nome ASC",
		    countQuery = "SELECT COUNT(DISTINCT u) FROM Usuario u WHERE u.nome LIKE %:usuarioNome% AND u.email LIKE %:usuarioEmail% AND (:usuarioStatus IS NULL OR u.status = :usuarioStatus) AND (:usuarioCategoria IS NULL OR :usuarioCategoria MEMBER OF u.categorias) AND (:usuarioExportado IS NULL OR u.exportado = :usuarioExportado)"
		)
		Page<UsuarioPesquisaProjection> listarUsuariosPesquisaProjetado(
		    @Param("usuarioNome") String usuarioNome,
		    @Param("usuarioEmail") String usuarioEmail,
		    @Param("usuarioStatus") Status usuarioStatus,
		    @Param("usuarioCategoria") Categoria usuarioCategoria,
		    @Param("usuarioExportado") Boolean usuarioExportado,
		    Pageable pageable
		);
}
