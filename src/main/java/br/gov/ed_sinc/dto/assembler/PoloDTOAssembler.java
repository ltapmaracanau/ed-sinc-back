package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.PoloDTO;
import br.gov.ed_sinc.model.Polo;


@Component
public class PoloDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<PoloDTO> toPageModel(Page<Polo> poloPage) {
	    List<PoloDTO> dtoList = toCollectionModel(poloPage.getContent());
	    return new PageImpl<>(dtoList, poloPage.getPageable(), poloPage.getTotalElements());
	}
	
	public PoloDTO toModel(Polo polo) {
		return modelMapper.map(polo, PoloDTO.class);
	}
	
	public List<PoloDTO> toCollectionModel(Collection<Polo> polos) {
		return polos.stream()
				.map(polo -> toModel(polo))
				.collect(Collectors.toList());
	}
	
}