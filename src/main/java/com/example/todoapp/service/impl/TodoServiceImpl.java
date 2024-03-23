package com.example.todoapp.service.impl;

import com.example.todoapp.entity.ToDo;
import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    private TodoRepository todoRepository;
    private ModelMapper modelMapper;

    @Override
    public TodoDto addTodo(TodoDto todoDto) {
        // covert TodoDto into Todo jpa entity
        // convert object for another class with the same instance
        ToDo todo = modelMapper.map(todoDto, ToDo.class);


        ToDo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDto.class);
    }

    @Override
    public TodoDto findTodo(Long id) {
        ToDo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public List<TodoDto> getAllTodos() {
        List<ToDo> list = todoRepository.findAll();
        return list.stream().map(item -> modelMapper.map(item, TodoDto.class)).collect(Collectors.toList());
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {
        ToDo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("To Do Not Found:" + id));
        todo.setDescription(todoDto.getDescription());
        todo.setTitle(todoDto.getTitle());
        todo.setCompleted(todoDto.isCompleted());
        todoRepository.save(todo);
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("To do Not Found:" + id));
        todoRepository.deleteById(id);
    }

    @Override
    public TodoDto completeDto(Long id) {
        ToDo toDo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("To DO Not Found:" + id));
        toDo.setCompleted(Boolean.TRUE);
        ToDo updateTodo = todoRepository.save(toDo);
        return modelMapper.map(updateTodo, TodoDto.class);
    }

    @Override
    public TodoDto incompleteDto(Long id) {
        ToDo toDo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("To DO Not Found:" + id));
        toDo.setCompleted(Boolean.FALSE);
        ToDo updateTodo = todoRepository.save(toDo);
        return modelMapper.map(updateTodo, TodoDto.class);
    }

}
