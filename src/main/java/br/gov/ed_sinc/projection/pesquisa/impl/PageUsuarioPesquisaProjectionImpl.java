package br.gov.ed_sinc.projection.pesquisa.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import br.gov.ed_sinc.projection.pesquisa.PageUsuarioPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.UsuarioPesquisaProjection;

public class PageUsuarioPesquisaProjectionImpl implements PageUsuarioPesquisaProjection {
    private final Page<UsuarioPesquisaProjection> page;

    public PageUsuarioPesquisaProjectionImpl(Page<UsuarioPesquisaProjection> page) {
        this.page = page;
    }

    @Override
    public List<UsuarioPesquisaProjection> getContent() {
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