package com.gamecenter.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamecenter.domain.Usuario;
import com.gamecenter.dto.UsuarioDTO;
import com.gamecenter.repository.UsuarioRepository;

@Service
public class UsuarioService {
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public void save(UsuarioDTO usuarioDTO) {
		String login = usuarioDTO.getLogin();
		String senha = usuarioDTO.getSenha();
		String nickname = usuarioDTO.getNickname();
		String email = usuarioDTO.getEmail();
		LocalDate nascimento = usuarioDTO.getNascimento();
		
		Usuario usuario = new Usuario(login, senha, nickname, email, nascimento);
		validarUsuario(usuario);
		
		this.usuarioRepository.saveAndFlush(usuario);
		usuarioDTO.setId(usuario.getId());
	}

	private void validarUsuario(Usuario usuario) {
		Optional<Usuario> userFound = usuarioRepository.findByLogin(usuario.getLogin());
		if (userFound.isPresent()) {
			throw new ServiceException("Usuário já cadastrado");
		}
	}
	
	public Usuario findById(Integer id) {
		Optional<Usuario> userFound = usuarioRepository.findById(id);
		if (userFound.isPresent()) {
			userFound.get();
		}
		throw new ServiceException("Usuário não encontrado");
	}
	
	public Usuario findByLogin(String login) {
		Optional<Usuario> userFound = usuarioRepository.findByLogin(login);
		if (userFound.isPresent()) {
			userFound.get();
		}
		throw new ServiceException("Usuário não encontrado");
	}
	
	public void update(UsuarioDTO usuarioDTO) {
		Integer id = usuarioDTO.getId();
		String login = usuarioDTO.getLogin();
		String senha = usuarioDTO.getSenha();
		String nickname = usuarioDTO.getNickname();
		String email = usuarioDTO.getEmail();
		LocalDate nascimento = usuarioDTO.getNascimento();
		
		Usuario usuario = new Usuario(id, login, senha, nickname, email, nascimento);
		this.usuarioRepository.saveAndFlush(usuario);
	}
	
	public void delete(Integer id) {
		this.usuarioRepository.deleteById(id);
	}
	
	public void deleteAll() {
		this.usuarioRepository.deleteAll();
	}
	
	public List<UsuarioDTO> findAll() {
		List<UsuarioDTO> userReturn = new ArrayList<UsuarioDTO>();
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		for(Usuario usuario : usuarios) {
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setId(usuario.getId());
			usuarioDTO.setLogin(usuario.getLogin());
			usuarioDTO.setSenha(usuario.getSenha());
			usuarioDTO.setNickname(usuario.getNickname());
			usuarioDTO.setEmail(usuario.getEmail());
			usuarioDTO.setNascimento(usuario.getNascimento());
			
			userReturn.add(usuarioDTO);
		}
		
		return userReturn;
	}
}