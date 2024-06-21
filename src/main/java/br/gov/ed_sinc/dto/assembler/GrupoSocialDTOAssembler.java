package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.GrupoSocialDTO;
import br.gov.ed_sinc.model.GrupoSocial;


@Component
public class GrupoSocialDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<GrupoSocialDTO> toPageModel(Page<GrupoSocial> grupoSocialPage) {
	    List<GrupoSocialDTO> dtoList = toCollectionModel(grupoSocialPage.getContent());
	    return new PageImpl<>(dtoList, grupoSocialPage.getPageable(), grupoSocialPage.getTotalElements());
	}
	
	public GrupoSocialDTO toModel(GrupoSocial grupoSocial) {
		return modelMapper.map(grupoSocial, GrupoSocialDTO.class);
	}
	
	public List<GrupoSocialDTO> toCollectionModel(Collection<GrupoSocial> grupoSocials) {
		return grupoSocials.stream()
				.map(grupoSocial -> toModel(grupoSocial))
				.collect(Collectors.toList());
	}
	
}
