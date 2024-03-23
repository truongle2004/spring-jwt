package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
// * allow client to access all the rest api
@RestController
@RequestMapping("api/todos")
@AllArgsConstructor
public class TodoController {
    private TodoService todoService;


    @PreAuthorize("hasRole('ADMIN')") // this annotation has the same as @Secured (“ROLE_VIEWER”)
    // for instance:
    // @Secured({ "ROLE_VIEWER", "ROLE_EDITOR" })
    // public boolean isValidUsername(String username) {
    // return userRoleRepository.isValidUsername(username);
    @PostMapping
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) {
        TodoDto savedToDoDto = todoService.addTodo(todoDto);
        return new ResponseEntity<>(savedToDoDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // this annotation equal to authorize.requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN", "USER"); method
    @GetMapping("{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable("id") Long TodoId) {
        TodoDto todoDto = todoService.findTodo(TodoId);
        return new ResponseEntity<>(todoDto, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllToDo() {
        List<TodoDto> list = todoService.getAllTodos();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // this annotation equal to authorize.requestMatchers(HttpMethod.PUT,"/api/**").hasRole("ADMIN"); method
    @PutMapping("{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable Long id) {
        TodoDto updateTodo = todoService.updateTodo(todoDto, id);
        return ResponseEntity.ok(updateTodo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete to do successfully");
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("{id}/complete")
    public ResponseEntity<TodoDto> setCompleteTodo(@PathVariable Long id) {
        TodoDto updatedTodo = todoService.completeDto(id);
        return ResponseEntity.ok(updatedTodo);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("{id}/in-complete")
    public ResponseEntity<TodoDto> setInCompleteTodo(@PathVariable Long id) {
        TodoDto updatedTodo = todoService.incompleteDto(id);
        return ResponseEntity.ok(updatedTodo);
    }
}
