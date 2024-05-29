package br.gov.ed_sinc.projection.pesquisa;

import java.util.List;

import br.gov.ed_sinc.projection.GenericoIdNomeProjection;

public interface PageUsuarioPesquisaProjection {
	List<GenericoIdNomeProjection> getContent();
    Long getNumber();
    Long getSize();
    Long getTotalElements(); 
    Long getTotalPages();
}