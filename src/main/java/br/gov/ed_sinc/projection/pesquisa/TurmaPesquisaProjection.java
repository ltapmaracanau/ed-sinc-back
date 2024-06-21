package br.gov.ed_sinc.projection.pesquisa;

import java.util.List;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Status;

public interface TurmaPesquisaProjection {
	Long getId();
	String getNome();
	String getDescricao();
	Status getStatus();
	Boolean getExportado();
	List<Usuario> getUsuarios();
}
