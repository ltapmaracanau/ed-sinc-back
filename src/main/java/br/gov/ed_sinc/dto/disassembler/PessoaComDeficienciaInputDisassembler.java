package br.gov.ed_sinc.dto.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.PessoaComDeficienciaInput;
import br.gov.ed_sinc.model.PessoaComDeficiencia;

@Component
public class PessoaComDeficienciaInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public PessoaComDeficiencia toDomainObject(PessoaComDeficienciaInput pessoaComDeficienciaInput) {
		return modelMapper.map(pessoaComDeficienciaInput, PessoaComDeficiencia.class);
	}
	
	public void copyToDomainObject(PessoaComDeficienciaInput pessoaComDeficienciaInput, PessoaComDeficiencia pessoaComDeficiencia) {
		modelMapper.map(pessoaComDeficienciaInput, pessoaComDeficiencia);
	}
	
}