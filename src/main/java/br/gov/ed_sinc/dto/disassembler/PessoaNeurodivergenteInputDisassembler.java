package br.gov.ed_sinc.dto.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.PessoaNeurodivergenteInput;
import br.gov.ed_sinc.model.PessoaNeurodivergente;

@Component
public class PessoaNeurodivergenteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public PessoaNeurodivergente toDomainObject(PessoaNeurodivergenteInput pessoaNeurodivergenteInput) {
		return modelMapper.map(pessoaNeurodivergenteInput, PessoaNeurodivergente.class);
	}
	
	public void copyToDomainObject(PessoaNeurodivergenteInput pessoaNeurodivergenteInput, PessoaNeurodivergente pessoaNeurodivergente) {
		modelMapper.map(pessoaNeurodivergenteInput, pessoaNeurodivergente);
	}
	
}