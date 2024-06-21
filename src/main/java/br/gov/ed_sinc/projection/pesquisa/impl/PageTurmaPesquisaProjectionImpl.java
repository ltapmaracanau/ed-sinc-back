package br.gov.ed_sinc.projection.pesquisa.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import br.gov.ed_sinc.projection.pesquisa.PageTurmaPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.TurmaPesquisaProjection;

public class PageTurmaPesquisaProjectionImpl implements PageTurmaPesquisaProjection{
    private final Page<TurmaPesquisaProjection> page;

    public PageTurmaPesquisaProjectionImpl(Page<TurmaPesquisaProjection> page) {
        this.page = page;
    }

    @Override
    public List<TurmaPesquisaProjection> getContent() {
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