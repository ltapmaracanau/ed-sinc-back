package br.gov.ed_sinc.dto.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.GrupoSocialInput;
import br.gov.ed_sinc.model.GrupoSocial;

@Component
public class GrupoSocialInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public GrupoSocial toDomainObject(GrupoSocialInput grupoSocialInput) {
		return modelMapper.map(grupoSocialInput, GrupoSocial.class);
	}
	
	public void copyToDomainObject(GrupoSocialInput grupoSocialInput, GrupoSocial grupoSocial) {
		modelMapper.map(grupoSocialInput, grupoSocial);
	}
	
}
