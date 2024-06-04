package br.gov.ed_sinc.dto.disassembler;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.UsuarioInput;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;

@Component
public class UsuarioInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Usuario toDomainObject(UsuarioInput usuarioInput) {
		return modelMapper.map(usuarioInput, Usuario.class);
	}
	
	public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
		usuario.setCategorias(new ArrayList<Categoria>());
		modelMapper.map(usuarioInput, usuario);
	}
	
}