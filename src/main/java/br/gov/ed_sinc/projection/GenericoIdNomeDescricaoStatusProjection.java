package br.gov.ed_sinc.projection;

import br.gov.ed_sinc.model.enums.Status;

public interface GenericoIdNomeDescricaoStatusProjection {
	Long getId();
    String getNome();
    String getDescricao();
    Status getStatus();
}