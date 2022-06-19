package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.mballem.curso.security.service.UsuarioService;

@Controller
public class HomeController extends AuxiliarController {

	@Autowired
	private UsuarioService service;

	/*
	 * @Autowired private ProfileUsuarioService serviceUsuario;
	 * 
	 * 
	 * @Autowired private ProfileAnalistaService serviceAnalista;
	 */
	
	// Abrir pagina home
	@GetMapping({ "/home" })
	public String home(HttpSession session, ModelMap model) {

		return "home";
	}

	/*
	// abrir pagina perfil analista ou admin
	@GetMapping({ "/meuPerfil" })
	public String meuPerfil(ProfileUsuario profileUsuario, ProfileAnalista profileAnalista) {

		// Verificar usuario logado pelo email
		Optional<Usuario> usuario = service
				.buscarPorEmailEAtivo(SecurityContextHolder.getContext().getAuthentication().getName());

		if (usuario.get().getPerfis().contains(new Perfil(PerfilTipo.SUPERVISOR.getCod()))
				|| usuario.get().getPerfis().contains(new Perfil(PerfilTipo.COORDENADOR.getCod()))
				|| usuario.get().getPerfis().contains(new Perfil(PerfilTipo.GERENTE.getCod()))
				|| usuario.get().getPerfis().contains(new Perfil(PerfilTipo.ADMINISTRADOR.getCod()))) {

			return "/profileAdministradores/cadastroDadosPessoais";

		} else {

			return "redirect:/analista/dados";
		}

	}
*/
	// abrir pagina login
	@GetMapping({ "/", "/login" })
	public String login() {
		return "login";
	}

	// sair
	@GetMapping({ "/logout" })
	public String sair(HttpSession session) {
		session.invalidate();
		return "login";
	}

	// login invalido
	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Crendenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acesso Negado");
		model.addAttribute("message", "Você não tem permissão para acesso a esta área ou ação.");
		return "error";
	}
}
