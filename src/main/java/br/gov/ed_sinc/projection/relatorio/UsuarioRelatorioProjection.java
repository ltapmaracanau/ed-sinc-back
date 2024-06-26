package br.gov.ed_sinc.projection.relatorio;

import java.util.List;

import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;

public interface UsuarioRelatorioProjection {
	Long getId();
	String getNome();
	String getEmail();
	String getCpf();
	String getTelefone();
	Status getStatus();
	List<Categoria> getCategorias();
}