package br.gov.ed_sinc.dto.disassembler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.dto.input.UsuarioInput;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.GrupoSocial;
import br.gov.ed_sinc.model.PessoaComDeficiencia;
import br.gov.ed_sinc.model.PessoaNeurodivergente;
import br.gov.ed_sinc.model.Polo;

@Component
public class UsuarioInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Usuario toDomainObject(UsuarioInput usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }
    
    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        usuario.setCategorias(new ArrayList<Categoria>());
        initializeListIfNecessary(usuario.getGruposSociais(), usuarioInput.getGruposSociais(), usuario::setGruposSociais, GrupoSocial.class);
        initializeListIfNecessary(usuario.getPolos(), usuarioInput.getPolos(), usuario::setPolos, Polo.class);
        initializeListIfNecessary(usuario.getPessoasComDeficiencia(), usuarioInput.getPessoasComDeficiencia(), usuario::setPessoasComDeficiencia, PessoaComDeficiencia.class);
        initializeListIfNecessary(usuario.getPessoasNeurodivergente(), usuarioInput.getPessoasNeurodivergente(), usuario::setPessoasNeurodivergente, PessoaNeurodivergente.class);

        modelMapper.map(usuarioInput, usuario);
    }

    private <T, U> void initializeListIfNecessary(List<T> currentList, List<U> inputList, Consumer<List<T>> setter, Class<T> clazz) {
        if (currentList != null) {
            if (inputList != null) {
                List<T> transformedList = inputList.stream()
                    .map(inputItem -> modelMapper.map(inputItem, clazz))
                    .collect(Collectors.toList());
                setter.accept(new ArrayList<>(transformedList));
            } else {
                setter.accept(null);
            }
        } else {
            setter.accept(null);
        }
    }
}
