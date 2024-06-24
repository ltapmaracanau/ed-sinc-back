package br.gov.ed_sinc.projection.pesquisa;

import java.time.LocalDate;
import java.util.List;

import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Raca;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.GenericoIdNomeProjection;

public interface UsuarioPesquisaProjection {
	Long getId();
	String getNome();
	String getEmail();
	String getCpf();
	String getTelefone();
	LocalDate getDataNascimento();
	Status getStatus();
	Boolean getExportado();
	Raca getRaca();
	List<Categoria> getCategorias();
	List<GenericoIdNomeProjection> getGruposSociais();
	List<GenericoIdNomeProjection> getPessoasComDeficiencia();
	List<GenericoIdNomeProjection> getPessoasNeurodivergente();
	List<GenericoIdNomeProjection> getPolos();
}
