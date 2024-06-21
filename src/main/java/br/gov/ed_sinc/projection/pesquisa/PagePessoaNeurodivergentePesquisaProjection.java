package br.gov.ed_sinc.projection.pesquisa;

import java.util.List;

import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;

public interface PagePessoaNeurodivergentePesquisaProjection {
	List<GenericoIdNomeDescricaoStatusProjection> getContent();
    Long getNumber();
    Long getSize();
    Long getTotalElements(); 
    Long getTotalPages();
}