package br.gov.ed_sinc.projection.pesquisa;

import java.util.List;

public interface PageUsuarioPesquisaProjection {
	List<UsuarioPesquisaProjection> getContent();
    Long getNumber();
    Long getSize();
    Long getTotalElements(); 
    Long getTotalPages();
}