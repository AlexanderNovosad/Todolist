package com.exercise.todolist.controller;

import com.exercise.todolist.model.Task;
import com.exercise.todolist.model.TasksList;
import com.exercise.todolist.model.enums.Status;
import com.exercise.todolist.repository.TaskRepository;
import com.exercise.todolist.repository.TasksListRepository;
import org.codehaus.groovy.transform.SortableASTTransformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TasksListRepository tasksListRepository;

    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        Status[] statuses = Status.values();
        model.addAttribute("statuses", statuses);
        List<Task> tasks = taskRepository.findAllByOrderByPriorityAsc();
        model.addAttribute("tasks", tasks);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "tasks";
    }

    @GetMapping("/addTask")
    public String getAddTaskForm(Model model) {
        Task taskForm = new Task();
        model.addAttribute("taskForm",taskForm);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "addTask";
    }

    @PostMapping("/addTask")
    public String createTask(Model model, @ModelAttribute("taskForm") @Valid @RequestBody Task task, BindingResult bindingResult, @RequestParam("tasksList") Long tasksListId) {
        if (bindingResult.hasErrors()) {
            List<TasksList> tasksLists = tasksListRepository.findAll();
            model.addAttribute("tasksLists", tasksLists);
            return "addTask";
        }
        Task newTask = new Task();
        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setPriority(task.getPriority());
        newTask.setCreatedAt(Calendar.getInstance().getTime());
        newTask.setStatus(Status.CONTINUE);
        newTask.setTasksList(tasksListRepository.findOne(tasksListId));
        taskRepository.save(newTask);
        return "redirect:/tasks";
    }

    @GetMapping("/searchTaskByName")
    public String getTasksByName(Model model, @RequestParam("searchTaskName") String taskName) {
        List<Task> tasks = taskRepository.findByNameLike(taskName+"%");
        if(tasks.isEmpty()) {
            return "redirect:/notFoundPage";
        }
        model.addAttribute("tasks", tasks);
        Status[] statuses = Status.values();
        model.addAttribute("statuses", statuses);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "tasks";
    }

    @GetMapping("/searchTaskByStatus")
    public String getTasksByStatus(Model model, @RequestParam("taskStatuses") Status status) {
        List<Task> tasks = taskRepository.findByStatusLike(status);
        if(tasks.isEmpty()) {
            return "redirect:/notFoundPage";
        }
        model.addAttribute("tasks", tasks);
        Status[] statuses = Status.values();
        model.addAttribute("statuses", statuses);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "tasks";
    }

    @GetMapping("/searchTaskByTasksList")
    public String getTasksByTasksList(Model model, @RequestParam("tasksLists") Long tasksListId) {
        List<Task> tasks = taskRepository.findByTasksListId(tasksListId);
        if(tasks.isEmpty()) {
            return "redirect:/notFoundPage";
        }
        model.addAttribute("tasks", tasks);
        Status[] statuses = Status.values();
        model.addAttribute("statuses", statuses);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "tasks";
    }

    @GetMapping("/editTask")
    public String getEditTaskForm(Model model, @RequestParam("taskId") Long id) {
        Task editTaskForm = taskRepository.findOne(id);
        model.addAttribute("editTaskForm",editTaskForm);
        List<TasksList> tasksLists = tasksListRepository.findAll();
        model.addAttribute("tasksLists", tasksLists);
        return "editTask";
    }

    @PostMapping("/editThisTask")
    public String updateTask(Model model, @RequestParam("taskId") Long id, @ModelAttribute("editTaskForm") @Valid @RequestBody Task taskDetails, BindingResult bindingResult, @RequestParam("tasksList") Long tasksListId) {
        if (bindingResult.hasErrors()) {
            List<TasksList> tasksLists = tasksListRepository.findAll();
            model.addAttribute("tasksLists", tasksLists);
            return "editTask";
        }
        Task task = taskRepository.findOne(id);
        task.setName(taskDetails.getName());
        task.setDescription(taskDetails.getDescription());
        task.setCompleteAt(taskDetails.getCompleteAt());
        task.setPriority(taskDetails.getPriority());
        task.setStatus(taskDetails.getStatus());
        task.setTasksList(tasksListRepository.findOne(tasksListId));
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/DeleteTask")
    public String deleteTask(@RequestParam("deleteTaskId") Long id) {
        Task task = taskRepository.findOne(id);
        if(task == null) {
            return "redirect:/notFoundPage";
        }
        taskRepository.delete(task);
        return "redirect:/tasks";
    }

    @PostMapping("/CompleteTask")
    public String completeTask(@RequestParam("completeTaskId") Long id) {
        Task task = taskRepository.findOne(id);
        if(task == null) {
            return "redirect:/notFoundPage";
        }
        task.setCompleteAt(Calendar.getInstance().getTime());
        task.setStatus(Status.COMPLETE);
        taskRepository.save(task);
        return "redirect:/tasks";
    }
}
