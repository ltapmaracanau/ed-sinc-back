package br.gov.ed_sinc.dto.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.PoloInput;
import br.gov.ed_sinc.model.Polo;

@Component
public class PoloInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Polo toDomainObject(PoloInput poloInput) {
		return modelMapper.map(poloInput, Polo.class);
	}
	
	public void copyToDomainObject(PoloInput poloInput, Polo polo) {
		modelMapper.map(poloInput, polo);
	}
	
}