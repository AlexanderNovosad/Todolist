package com.exercise.todolist.controller;

import com.exercise.todolist.model.TasksList;
import com.exercise.todolist.model.enums.Color;
import com.exercise.todolist.repository.TasksListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ListController {
    @Autowired
    TasksListRepository tasksListRepository;

    @GetMapping("/lists")
    public String getAllLists(Model model) {
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        Color[] colors = Color.values();
        model.addAttribute("colors", colors);
        return "lists";
    }

    @GetMapping("/addList")
    public String getAddListForm(Model model) {
        Color[] colors = Color.values();
        TasksList listForm = new TasksList();
        model.addAttribute("colors", colors);
        model.addAttribute("listForm",listForm);
        return "addList";
    }

    @PostMapping("/addList")
    public String createTasksList(Model model, @ModelAttribute("listForm") @Valid @RequestBody TasksList tasksList, BindingResult bindingResult, @RequestParam("color") Color color) {
        if (bindingResult.hasErrors()) {
            Color[] colors = Color.values();
            model.addAttribute("colors", colors);
            return "addList";
        }
        TasksList newTasksList = new TasksList(tasksList.getName(),color);
        tasksListRepository.save(newTasksList);
        return "redirect:/lists";
    }

    @GetMapping("/searchListByName")
    public String getTasksListsByName(Model model, @RequestParam("searchItem") String item) {
        List<TasksList> tasksLists = tasksListRepository.findByNameLike(item+"%");
        if(tasksLists.isEmpty()) {
            return "redirect:/notFoundPage";
        }
        model.addAttribute("tasksLists", tasksLists);
        Color[] colors = Color.values();
        model.addAttribute("colors", colors);
        return "lists";
    }

    @GetMapping("/searchListByColor")
    public String getTasksListsByColor(Model model, @RequestParam("listsColors") Color color) {
        List<TasksList> tasksLists = tasksListRepository.findByColorLike(color);
        if(tasksLists.isEmpty()) {
            return "redirect:/notFoundPage";
        }
        model.addAttribute("tasksLists", tasksLists);
        Color[] colors = Color.values();
        model.addAttribute("colors", colors);
        return "lists";
    }

    @GetMapping("/editList")
    public String getEditListForm(Model model, @RequestParam("listId") Long id) {
        Color[] colors = Color.values();
        TasksList editForm = tasksListRepository.findOne(id);
        model.addAttribute("colors", colors);
        model.addAttribute("editForm",editForm);
        return "editList";
    }

    @PostMapping("/edit")
    public String updateTasksList(Model model, @RequestParam("listId") Long id, @ModelAttribute("editForm") @Valid @RequestBody TasksList tasksListDetails, BindingResult bindingResult, @RequestParam("color") Color color) {
        if (bindingResult.hasErrors()) {
            Color[] colors = Color.values();
            model.addAttribute("colors", colors);
            return "editList";
        }
        TasksList tasksList = tasksListRepository.findOne(id);
        tasksList.setName(tasksListDetails.getName());
        tasksList.setColor(color);
        tasksListRepository.save(tasksList);
        return "redirect:/lists";
    }

    @PostMapping("/DeleteList")
    public String deleteTasksList(@RequestParam("deleteListId") Long id) {
        TasksList tasksList = tasksListRepository.findOne(id);
        if(tasksList == null) {
            return "redirect:/notFoundPage";
        }
        tasksListRepository.delete(tasksList);
        return "redirect:/lists";
    }
}
