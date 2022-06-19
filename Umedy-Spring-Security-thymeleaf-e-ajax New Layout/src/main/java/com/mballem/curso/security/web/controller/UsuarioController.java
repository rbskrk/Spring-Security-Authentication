package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Perfil;

import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	// abrir cadastro de usuarios
	// (Analista/Supervisor/Coordenador/Gerente/Administrador)
	@GetMapping("/novo/cadastro")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastroUsuario";
	}

	// salvar cadastro de usuarios pelo administrador
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();

		// verifica se o usuário tem somente um perfil
		if (perfis.size() > 1 || perfis.containsAll(
				Arrays.asList(new Perfil(1L), new Perfil(2L), new Perfil(3L), new Perfil(4L), new Perfil(5L)))) {
			attr.addFlashAttribute("falha", "O usuário não pode ter mais de um perfil");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				if (validaSenha(usuario)) {

					if (validaEmail(usuario)) {
						service.salvarUsuario(usuario);
						attr.addFlashAttribute("sucesso", " Operação realizada com sucesso!");
					} else {
						attr.addFlashAttribute("falha", " Este email não faz parte da organização");
					}

				} else {
					attr.addFlashAttribute("falha", "Senha invalida!");
				}

			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente.");
				return "redirect:/u/novo/cadastro";
			}
		}
		return "redirect:/u/novo/cadastro";
	}

	// valida email por regex
	private boolean validaEmail(Usuario usuario) {
		String email = usuario.getEmail();
		return email.matches("^([\\w\\-]+\\.)*[\\w\\- ]+@mosaicco.com$");

	}

	// valida senha por regex
	private boolean validaSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		return senha.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$");

	}

	// abrir lista de usuarios
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/listaUsuario";
	}

	// listar usuarios na datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}

	// pre edicao de credenciais de usuarios
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id, Usuario usuario) {
		return new ModelAndView("usuario/cadastroUsuario", "usuario", service.buscarPorId(id));
	}

	// excluir usuário por administrador
	@GetMapping("excluir/usuario/{id}")
	public String excluirUsuario(@PathVariable("id") Long id, RedirectAttributes attr) {

		try {
			service.deleteUsuarioPorId(id);
			attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");

			return "redirect:/u/lista";

		} catch (Exception e) {
			attr.addFlashAttribute("falha", "O Usuário não pode ser excluído");
		}

		return "redirect:/u/lista";
	}
	
	
}
