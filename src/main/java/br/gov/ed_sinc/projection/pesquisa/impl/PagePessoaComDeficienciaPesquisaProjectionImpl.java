package br.gov.ed_sinc.projection.pesquisa.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import br.gov.ed_sinc.projection.GenericoIdNomeDescricaoStatusProjection;
import br.gov.ed_sinc.projection.pesquisa.PagePessoaComDeficienciaPesquisaProjection;

public class PagePessoaComDeficienciaPesquisaProjectionImpl implements PagePessoaComDeficienciaPesquisaProjection{
    private final Page<GenericoIdNomeDescricaoStatusProjection> page;

    public PagePessoaComDeficienciaPesquisaProjectionImpl(Page<GenericoIdNomeDescricaoStatusProjection> page) {
        this.page = page;
    }

    @Override
    public List<GenericoIdNomeDescricaoStatusProjection> getContent() {
        return page.getContent();
    }

    @Override
    public Long getNumber() {
        return (long) page.getNumber();
    }

    @Override
    public Long getSize() {
        return (long) page.getSize();
    }

    @Override
    public Long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public Long getTotalPages() {
        return (long) page.getTotalPages();
    }
}