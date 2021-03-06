package br.org.generation.blogPessoal.seguranca;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.org.generation.blogPessoal.model.UsuarioModel;

public class UserDatailsImpl implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String passaword;
	private List<GrantedAuthority> authorities; 
	
		public UserDatailsImpl(UsuarioModel user) {
		this.userName = user.getUsuario();  
		this.passaword = user.getSenha();
	}

	public UserDatailsImpl() {}
		
	// metodos abaixo importados da extenção UserDatails 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return passaword;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
