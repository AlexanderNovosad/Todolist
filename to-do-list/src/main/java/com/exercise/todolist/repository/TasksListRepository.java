package com.exercise.todolist.repository;

import com.exercise.todolist.model.TasksList;
import com.exercise.todolist.model.enums.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksListRepository extends JpaRepository<TasksList, Long> {
    TasksList findOneByNameLike(String name);
    List<TasksList> findByNameLike(String name);
    List<TasksList> findByColorLike(Color color);
}
