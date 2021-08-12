package br.org.generation.blogPessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.generation.blogPessoal.model.Tema;
import br.org.generation.blogPessoal.repository.TemaRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tema")
public class TemaController {

	//injeta os serviços 
	@Autowired 
	private TemaRepository repository; 
	
	// retorna todos os dados 
	@GetMapping
	public ResponseEntity<List<Tema>> getALL(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	// retorna por id 
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable long id){
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	// retorna por noem 
	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Tema>> getByName(@PathVariable String descricao){
		return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(descricao)); 
	}
	
	// faz postagem 
	@PostMapping
	public ResponseEntity<Tema> post (@RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(repository.save(tema));
	}
	
	// faz alteração
	@PutMapping
	public ResponseEntity<Tema> pust (@RequestBody Tema tema){
		return ResponseEntity.ok(repository.save(tema));
	}
	
	// deleta. 
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		repository.deleteById(id); 
	}
}
