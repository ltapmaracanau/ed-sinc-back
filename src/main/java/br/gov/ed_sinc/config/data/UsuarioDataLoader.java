package br.gov.ed_sinc.config.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
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
        	usuario.setStatus(Status.Ativo);
        	usuario.setCategorias(new ArrayList<>(List.of(Categoria.Administrador)));
        	usuarioRepository.save(usuario);
        	
        	// Massa de teste
            List<Usuario> usuariosTeste = new ArrayList<>();
        	
            for (int i = 2; i <= 15; i++) {
                Usuario usuarioTeste = new Usuario();
                usuarioTeste.setNome("Usuario" + i);
                usuarioTeste.setEmail("usuario" + i + "@gmail.com");
                usuarioTeste.setSenha(passwordEncoder.encode("Abcd12345"));
                usuarioTeste.setStatus(Status.Ativo);
                usuarioTeste.setCategorias(new ArrayList<>(List.of(Categoria.Coordenador)));
                usuariosTeste.add(usuarioTeste);
            }
            
            Usuario usuarioTeste = new Usuario();
            usuarioTeste.setNome("UsuarioTeste");
            usuarioTeste.setEmail("usuarioTeste@gmail.com");
            usuarioTeste.setSenha(passwordEncoder.encode("Abcd12345"));
            usuarioTeste.setStatus(Status.Ativo);
            usuarioTeste.setCategorias(new ArrayList<>(List.of(Categoria.Coordenador, Categoria.Mentor)));
            usuariosTeste.add(usuarioTeste);
            usuarioRepository.save(usuarioTeste);
            
            usuarioRepository.saveAll(usuariosTeste);

        	System.out.println("Cadastro do Usuário Administrador e usuários de teste concluído.");
        }
    }
}
