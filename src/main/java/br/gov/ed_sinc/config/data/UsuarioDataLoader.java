package br.gov.ed_sinc.config.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.repository.UsuarioRepository;

@Component
public class UsuarioDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean cadastros = usuarioRepository.existsById((long) 1);
        
        if(!cadastros) {
        	Usuario usuario = new Usuario();
        	usuario.setNome("Administrador");
        	usuario.setEmail("mirandajoaoj@gmail.com");
        	usuario.setSenha(passwordEncoder.encode("Abcd12345"));
        	usuario.setCategorias(new ArrayList<Categoria>(List.of(Categoria.Administrador)));
        	usuarioRepository.save(usuario);
        	
        	System.out.println(usuario);
	    	
	        System.out.println("Cadastro do Usuário Administrador concluído.");
        }
    }
}
