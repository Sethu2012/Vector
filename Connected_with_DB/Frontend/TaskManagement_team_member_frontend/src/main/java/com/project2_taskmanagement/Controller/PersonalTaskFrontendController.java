package com.project2_taskmanagement.Controller;

import com.project2_taskmanagement.Model.PersonalTaskDTO;
import com.project2_taskmanagement.Service.PersonalTaskFrontendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/myTasks")
public class PersonalTaskFrontendController {

    private final PersonalTaskFrontendService taskService;

    public PersonalTaskFrontendController(PersonalTaskFrontendService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String showMyTasksPage(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login";
        }
        try {
            Map<String, List<PersonalTaskDTO>> tasks = taskService.getPersonalTasksForUser(token);
            model.addAttribute("pastDatesTasks", tasks.getOrDefault("pastDatesTasks", Collections.emptyList()));
            model.addAttribute("todayTasks", tasks.getOrDefault("todayTasks", Collections.emptyList()));
            model.addAttribute("thisWeekTasks", tasks.getOrDefault("thisWeekTasks", Collections.emptyList()));
            model.addAttribute("nextWeekTasks", tasks.getOrDefault("nextWeekTasks", Collections.emptyList()));
            model.addAttribute("laterTasks", tasks.getOrDefault("laterTasks", Collections.emptyList()));
            model.addAttribute("withoutDateTasks", tasks.getOrDefault("withoutDateTasks", Collections.emptyList()));
            model.addAttribute("activePage", "myTasks");
            model.addAttribute("jwtToken", token);
            model.addAttribute("personalTaskDTO", new PersonalTaskDTO());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load tasks: " + e.getMessage());
            model.addAttribute("pastDatesTasks", Collections.emptyList());
            model.addAttribute("todayTasks", Collections.emptyList());
            model.addAttribute("thisWeekTasks", Collections.emptyList());
            model.addAttribute("nextWeekTasks", Collections.emptyList());
            model.addAttribute("laterTasks", Collections.emptyList());
            model.addAttribute("withoutDateTasks", Collections.emptyList());
            model.addAttribute("activePage", "myTasks");
            model.addAttribute("jwtToken", token);
            model.addAttribute("personalTaskDTO", new PersonalTaskDTO());
        }
        return "myTasks";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute PersonalTaskDTO taskDTO, 
                            @RequestParam("token") String token, 
                            Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login";
        }

        try {
            taskService.createTask(taskDTO, token);
            return "redirect:/myTasks?token=" + token + "&success=Task created successfully";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create task: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(token, model);
        }
    }

    @PostMapping("/updateStatus/{taskId}")
    public String updateTaskStatus(@PathVariable Long taskId, 
                                  @RequestParam boolean isCompleted, 
                                  @RequestParam("token") String token, 
                                  Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login";
        }

        try {
            taskService.updateTaskStatus(taskId, isCompleted, token);
            return "redirect:/myTasks?token=" + token;
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update task status: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(token, model);
        }
    }

    @PostMapping("/updatePin/{taskId}")
    public String updateTaskPinStatus(@PathVariable Long taskId, 
                                     @RequestParam boolean isPinned, 
                                     @RequestParam("token") String token, 
                                     Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login";
        }

        try {
            taskService.updateTaskPinStatus(taskId, isPinned, token);
            return "redirect:/myTasks?token=" + token;
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update pin status: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(token, model);
        }
    }
}