package com.exercise.todolist.repository;

import com.exercise.todolist.model.Task;
import com.exercise.todolist.model.TasksList;
import com.exercise.todolist.model.enums.Color;
import com.exercise.todolist.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByNameLike(String name);
    List<Task> findByStatusLike(Status status);
    List<Task> findByTasksListId(Long id);
    List<Task> findAllByOrderByPriorityAsc();
}
