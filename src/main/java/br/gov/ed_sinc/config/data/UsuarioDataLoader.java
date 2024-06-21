package br.gov.ed_sinc.config.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.gov.ed_sinc.model.GrupoSocial;
import br.gov.ed_sinc.model.PessoaComDeficiencia;
import br.gov.ed_sinc.model.PessoaNeurodivergente;
import br.gov.ed_sinc.model.Polo;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.repository.GrupoSocialRepository;
import br.gov.ed_sinc.repository.PessoaComDeficienciaRepository;
import br.gov.ed_sinc.repository.PessoaNeurodivergenteRepository;
import br.gov.ed_sinc.repository.PoloRepository;
import br.gov.ed_sinc.repository.UsuarioRepository;

@Component
public class UsuarioDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GrupoSocialRepository grupoSocialRepository;
    private final PessoaNeurodivergenteRepository pessoaNeurodivergenteRepository;
    private final PessoaComDeficienciaRepository pessoaComDeficienciaRepository;
    private final PoloRepository poloRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDataLoader(UsuarioRepository usuarioRepository, GrupoSocialRepository grupoSocialRepository, PasswordEncoder passwordEncoder,
    		PessoaNeurodivergenteRepository pessoaNeurodivergenteRepository, PessoaComDeficienciaRepository pessoaComDeficienciaRepository, PoloRepository poloRepository) {
    	this.poloRepository = poloRepository;
    	this.pessoaComDeficienciaRepository = pessoaComDeficienciaRepository;
    	this.pessoaNeurodivergenteRepository = pessoaNeurodivergenteRepository;
    	this.grupoSocialRepository = grupoSocialRepository;
        this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean cadastrosUsuario = usuarioRepository.existsById((long) 1);
        boolean cadastrosGrupoSocial = grupoSocialRepository.existsById((long) 2);
        boolean cadastrosPessoaNeurodivergente = pessoaNeurodivergenteRepository.existsById((long) 2);
        boolean cadastrosPessoaComDeficiencia = pessoaComDeficienciaRepository.existsById((long) 2);
        boolean cadastrosPolo = poloRepository.existsById((long) 2);
        
        if(!cadastrosUsuario) {
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
        
        if(!cadastrosGrupoSocial) {
        	GrupoSocial grupo1 = new GrupoSocial();
        	grupo1.setNome("Comunidades ribeirinhas");
        	grupo1.setDescricao("Grupo Social oriundo de comunidades ribeirinhas.");
        	grupoSocialRepository.save(grupo1);
        	
        	GrupoSocial grupo2 = new GrupoSocial();
        	grupo2.setNome("Comunidades quilombolas");
        	grupo2.setDescricao("Grupo Social oriundo de comunidades quilombolas.");
        	grupoSocialRepository.save(grupo2);
        	
        	GrupoSocial grupo3 = new GrupoSocial();
        	grupo3.setNome("Comunidades de agricultura familiar");
        	grupo3.setDescricao("Grupo Social oriundo de comunidades de agricultura familiar.");
        	grupoSocialRepository.save(grupo3);
        	
        }
        
        if(!cadastrosPessoaNeurodivergente) {	
        	PessoaNeurodivergente pessoaNeurodivergente1 = new PessoaNeurodivergente();
        	pessoaNeurodivergente1.setNome("Autismo");
        	pessoaNeurodivergente1.setDescricao("Pessoa neurodivergente do tipo autista.");
        	pessoaNeurodivergenteRepository.save(pessoaNeurodivergente1);
        	
        	PessoaNeurodivergente pessoaNeurodivergente2 = new PessoaNeurodivergente();
        	pessoaNeurodivergente2.setNome("Transtorno do Déficit de Atenção com Hiperatividade (TDAH)");
        	pessoaNeurodivergente2.setDescricao("Pessoa neurodivergente do tipo TDAH.");
        	pessoaNeurodivergenteRepository.save(pessoaNeurodivergente2);	
        }
        
        if(!cadastrosPessoaComDeficiencia) {    	
        	PessoaComDeficiencia pessoaComDeficiencia1 = new PessoaComDeficiencia();
        	pessoaComDeficiencia1.setNome("Deficiência Visual");
        	pessoaComDeficiencia1.setDescricao("Pessoa com deficiência visual.");
        	pessoaComDeficienciaRepository.save(pessoaComDeficiencia1);

        	PessoaComDeficiencia pessoaComDeficiencia2 = new PessoaComDeficiencia();
        	pessoaComDeficiencia2.setNome("Deficiência Auditiva");
        	pessoaComDeficiencia2.setDescricao("Pessoa com deficiência auditiva.");
        	pessoaComDeficienciaRepository.save(pessoaComDeficiencia2);

        	PessoaComDeficiencia pessoaComDeficiencia3 = new PessoaComDeficiencia();
        	pessoaComDeficiencia3.setNome("Deficiência Física");
        	pessoaComDeficiencia3.setDescricao("Pessoa com deficiência física.");
        	pessoaComDeficienciaRepository.save(pessoaComDeficiencia3);

        	PessoaComDeficiencia pessoaComDeficiencia4 = new PessoaComDeficiencia();
        	pessoaComDeficiencia4.setNome("Deficiência Intelectual");
        	pessoaComDeficiencia4.setDescricao("Pessoa com deficiência intelectual.");
        	pessoaComDeficienciaRepository.save(pessoaComDeficiencia4);

        	PessoaComDeficiencia pessoaComDeficiencia5 = new PessoaComDeficiencia();
        	pessoaComDeficiencia5.setNome("Surdez");
        	pessoaComDeficiencia5.setDescricao("Pessoa com surdez.");
        	pessoaComDeficienciaRepository.save(pessoaComDeficiencia5);
        }
        
        if(!cadastrosPolo) {      	
        }
        
    }
}
