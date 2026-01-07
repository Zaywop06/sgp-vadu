package org.ud2.developers.SGPVADU.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ud2.developers.SGPVADU.service.IntServicePerfiles;

@Controller
@RequestMapping("/perfiles")
public class PerfilesController {

	@Autowired
	private IntServicePerfiles servicePerfiles;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		model.addAttribute("perfiles", servicePerfiles.obtenerPerfiles());
		return "perfiles/listaPerfiles";
	}
}
