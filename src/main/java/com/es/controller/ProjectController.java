package com.es.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.ProjectInfoDto;
import com.es.response.GetProjectResponse;
import com.es.service.ClientCredentialsService;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/estimation-tool/")
public class ProjectController {
    @Autowired
    JIRARestService jIRARestService;
    
    @Autowired
    ClientCredentialsService clientCredentialsService;

    @GetMapping("/abc")
    public String getEmployee() {

        return "index";
    }
    
    @GetMapping("/")
    public String getLogin() {

        return "index3";
    }

//    @GetMapping("/viewproject")
//    public String viewProject(Model model) {
//        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
//        String response = jIRARestService.getAllProjects();
//        System.out.println("hittttt ::: "+ response);
//        return "index";
//    }
//    
//    @GetMapping("/viewprojectL")
//    public String viewProjectL(Model model) {
//        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
//        String response = jIRARestService.getAllProjects();
//        System.out.println("LLLL ::: "+ response);
//        return "index3";
//    }

    @GetMapping("getAllProjects/{id}")
    public GetProjectResponse getAllProjects(@PathVariable String id) {
        GetProjectResponse response = new GetProjectResponse();
        ArrayList<ProjectInfoDto> list = new ArrayList<>();

        try {
            list.addAll(jIRARestService.getAllProjects(Integer.parseInt(id)));

            if (list != null && !list.isEmpty()) {
                response.setCode(200); // Use HttpStatus constants
                response.setMessage("success");
                response.setData(list);
            } else {
                response.setCode(404); // Use HttpStatus constants
                response.setMessage("invalid");
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., log the error)
            e.printStackTrace(); // Log the exception or handle it based on your requirements.
            response.setCode(500); // Use HttpStatus constants
            response.setMessage("error");
        }

        return response;
    }


}
