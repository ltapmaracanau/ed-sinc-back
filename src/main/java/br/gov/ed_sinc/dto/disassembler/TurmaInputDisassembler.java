package br.gov.ed_sinc.dto.disassembler;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.TurmaInput;
import br.gov.ed_sinc.model.Turma;
import br.gov.ed_sinc.model.Usuario;

@Component
public class TurmaInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Turma toDomainObject(TurmaInput turmaInput) {
		return modelMapper.map(turmaInput, Turma.class);
	}
	
	public void copyToDomainObject(TurmaInput turmaInput, Turma turma) {
		turma.setUsuarios(new ArrayList<Usuario>());
		modelMapper.map(turmaInput, turma);
	}
	
}