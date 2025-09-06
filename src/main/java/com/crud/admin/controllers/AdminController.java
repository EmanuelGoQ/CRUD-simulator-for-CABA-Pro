package com.crud.admin.controllers;

import com.crud.admin.model.Admin;
import com.crud.admin.repository.AdminRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;
    
    // Actividad 1: Vista inicial
    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    // Actividad 2: Formulario de creación
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "create-admin";
    }
    
    // Actividad 3: Inserción de objeto
    @PostMapping("/save")
    public String createAdmin(@Valid @ModelAttribute Admin admin, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        
        // Validar si el email ya existe
        if (adminRepository.existsByEmail(admin.getEmail())) {
            result.rejectValue("email", "error.admin", "Este email ya está registrado");
        }
        
        if (result.hasErrors()) {
            return "create-admin";
        }
        
        try {
            adminRepository.save(admin);
            redirectAttributes.addFlashAttribute("message", "Elemento creado satisfactoriamente");
            return "redirect:/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error al crear el administrador");
            return "redirect:/create";
        }
    }
    
    // Actividad 4: Listar objetos
    @GetMapping("/list")
    public String listAdmins(Model model) {
        List<Admin> admins = adminRepository.findAll();
        model.addAttribute("admins", admins);
        return "list-admins";
    }
    
    // Actividad 5: Ver un objeto
    @GetMapping("/view/{id}")
    public String viewAdmin(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        
        if (adminOptional.isPresent()) {
            model.addAttribute("admin", adminOptional.get());
            return "view-admin";
        } else {
            redirectAttributes.addFlashAttribute("message", "Administrador no encontrado");
            return "redirect:/list";
        }
    }
    
    // Actividad 6: Borrar objeto
    @PostMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (adminRepository.existsById(id)) {
                adminRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("message", "Administrador eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("message", "Administrador no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error al eliminar el administrador");
        }
        
        return "redirect:/list";
    }
}
