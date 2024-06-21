package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.TurmaDTO;
import br.gov.ed_sinc.model.Turma;


@Component
public class TurmaDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<TurmaDTO> toPageModel(Page<Turma> turmaPage) {
	    List<TurmaDTO> dtoList = toCollectionModel(turmaPage.getContent());
	    return new PageImpl<>(dtoList, turmaPage.getPageable(), turmaPage.getTotalElements());
	}
	
	public TurmaDTO toModel(Turma turma) {
		return modelMapper.map(turma, TurmaDTO.class);
	}
	
	public List<TurmaDTO> toCollectionModel(Collection<Turma> turmas) {
		return turmas.stream()
				.map(turma -> toModel(turma))
				.collect(Collectors.toList());
	}
	
}