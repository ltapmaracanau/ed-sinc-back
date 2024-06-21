package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.PessoaNeurodivergenteDTO;
import br.gov.ed_sinc.model.PessoaNeurodivergente;


@Component
public class PessoaNeurodivergenteDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<PessoaNeurodivergenteDTO> toPageModel(Page<PessoaNeurodivergente> pessoaNeurodivergentePage) {
	    List<PessoaNeurodivergenteDTO> dtoList = toCollectionModel(pessoaNeurodivergentePage.getContent());
	    return new PageImpl<>(dtoList, pessoaNeurodivergentePage.getPageable(), pessoaNeurodivergentePage.getTotalElements());
	}
	
	public PessoaNeurodivergenteDTO toModel(PessoaNeurodivergente pessoaNeurodivergente) {
		return modelMapper.map(pessoaNeurodivergente, PessoaNeurodivergenteDTO.class);
	}
	
	public List<PessoaNeurodivergenteDTO> toCollectionModel(Collection<PessoaNeurodivergente> pessoaNeurodivergentes) {
		return pessoaNeurodivergentes.stream()
				.map(pessoaNeurodivergente -> toModel(pessoaNeurodivergente))
				.collect(Collectors.toList());
	}
	
}