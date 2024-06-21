package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.PessoaComDeficienciaDTO;
import br.gov.ed_sinc.model.PessoaComDeficiencia;


@Component
public class PessoaComDeficienciaDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<PessoaComDeficienciaDTO> toPageModel(Page<PessoaComDeficiencia> pessoaComDeficienciaPage) {
	    List<PessoaComDeficienciaDTO> dtoList = toCollectionModel(pessoaComDeficienciaPage.getContent());
	    return new PageImpl<>(dtoList, pessoaComDeficienciaPage.getPageable(), pessoaComDeficienciaPage.getTotalElements());
	}
	
	public PessoaComDeficienciaDTO toModel(PessoaComDeficiencia pessoaComDeficiencia) {
		return modelMapper.map(pessoaComDeficiencia, PessoaComDeficienciaDTO.class);
	}
	
	public List<PessoaComDeficienciaDTO> toCollectionModel(Collection<PessoaComDeficiencia> pessoaComDeficiencias) {
		return pessoaComDeficiencias.stream()
				.map(pessoaComDeficiencia -> toModel(pessoaComDeficiencia))
				.collect(Collectors.toList());
	}
	
}