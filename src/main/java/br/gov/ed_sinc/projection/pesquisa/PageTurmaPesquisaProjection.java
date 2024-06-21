package br.gov.ed_sinc.projection.pesquisa;

import java.util.List;

public interface PageTurmaPesquisaProjection {
	List<TurmaPesquisaProjection> getContent();
    Long getNumber();
    Long getSize();
    Long getTotalElements(); 
    Long getTotalPages();
}