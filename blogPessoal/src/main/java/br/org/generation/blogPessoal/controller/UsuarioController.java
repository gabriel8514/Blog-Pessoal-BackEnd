package br.org.generation.blogPessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.generation.blogPessoal.model.UsuarioLogin;
import br.org.generation.blogPessoal.model.UsuarioModel;
import br.org.generation.blogPessoal.repository.UsuarioRepository;
import br.org.generation.blogPessoal.service.UsuarioService;

@RestController 
@RequestMapping ("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@GetMapping("/all")
	public ResponseEntity<List<UsuarioModel>> getAll(){
		return ResponseEntity.ok(usuarioRepository.findAll()); 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioModel> getById(@PathVariable long id){
		return usuarioRepository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> Autentication(@RequestBody Optional<UsuarioLogin> user){
		return usuarioService.Logar(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); 
	}
	
	@PostMapping("/cadastrar")
    public ResponseEntity<Optional<UsuarioModel>> Post(@RequestBody UsuarioModel usuario) {

        Optional<UsuarioModel> usuarioResp = usuarioService.CadastrarUsuario(usuario);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        	        	
        }
    }
	
	@PutMapping("/alterar")
	public ResponseEntity<UsuarioModel> put (@RequestBody UsuarioModel usuario){
		Optional<UsuarioModel> updateUsuario = usuarioService.atualizarUsuario(usuario);
		try {
			return ResponseEntity.ok(updateUsuario.get());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build(); 
		}
	}
}
