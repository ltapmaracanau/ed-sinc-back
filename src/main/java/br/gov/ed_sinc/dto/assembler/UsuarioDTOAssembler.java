package br.gov.ed_sinc.dto.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.UsuarioDTO;
import br.gov.ed_sinc.model.Usuario;

@Component
public class UsuarioDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<UsuarioDTO> toPageModel(Page<Usuario> usuarioPage) {
	    List<UsuarioDTO> dtoList = toCollectionModel(usuarioPage.getContent());
	    return new PageImpl<>(dtoList, usuarioPage.getPageable(), usuarioPage.getTotalElements());
	}
	
	public UsuarioDTO toModel(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioDTO.class);
	}
	
	public List<UsuarioDTO> toCollectionModel(Collection<Usuario> usuarios) {
		return usuarios.stream()
				.map(usuario -> toModel(usuario))
				.collect(Collectors.toList());
	}
	
}