package br.org.generation.blogPessoal.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.org.generation.blogPessoal.model.UsuarioLogin;
import br.org.generation.blogPessoal.model.UsuarioModel;
import br.org.generation.blogPessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Optional<UsuarioModel> CadastrarUsuario(UsuarioModel usuario) {

		// Lança uma Exception do tipo Response Status Bad Request se o usuário já
		// existir
		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

		// Calcula a idade (em anos) através do método between, da Classe Period
		int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();

		// Verifica se a iade é menor de 18. Caso positivo, Lança uma Exception do tipo
		// Response Status Bad Request

		if (idade < 18)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário menor de 18 anos", null);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);

		return Optional.of(repository.save(usuario));
	}

	public Optional<UsuarioModel> atualizarUsuario(UsuarioModel usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {

			// Mesma verificação do método cadastrarUsuario
			int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();

			if (idade < 18)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário menor de 18 anos", null);

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			String senhaEncoder = encoder.encode(usuario.getSenha());
			usuario.setSenha(senhaEncoder);

			return Optional.of(repository.save(usuario));

		} else {

			// Lança uma Exception do tipo Response Status Not Found
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);

		}

	}

	public Optional<UsuarioLogin> Logar(Optional<UsuarioLogin> usuarioLogin) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<UsuarioModel> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());

		if (usuario.isPresent()) {
			if (encoder.matches(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

				String auth = usuarioLogin.get().getUsuario() + ":" + usuarioLogin.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);

				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				usuarioLogin.get().setToken(authHeader);

				return usuarioLogin;

			}
		}
		// Lanço uma Exception do tipo Response Status Unauthorized
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
	}
}
