package com.in28minutes.springboot.web.controller;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@SessionAttributes("name")
public class TodoController {

    @Autowired
    TodoService service;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        // Date - dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // from now on, whenever using date class, using this simple format
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value="/list-todos", method = RequestMethod.GET)
    public String showTodo(ModelMap model){
        String name = (String) model.get("name"); // get date in @SessionAttributes("name")
        model.put("todos", service.retrieveTodos(name));
        return "list-todos";
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap model){
        model.addAttribute("todo", new Todo(0, (String) model.get("name"), "", new Date(), false));
        return "todo";
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.POST)
    public String addTodos(ModelMap model, @Valid Todo todo, BindingResult result){
        if(result.hasErrors()){
            return "todo";
        }
        service.addTodo((String) model.get("name"), todo.getDesc(), todo.getTargetDate(), false);
        return "redirect:/list-todos";
    }

    @RequestMapping(value="/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam int id){
        service.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @RequestMapping(value="/update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result){
        todo.setUser((String) model.get("name"));
        if(result.hasErrors()){
            return "todo";
        }

        service.updateTodos(todo);

        return "redirect:/list-todos";
    }

    @RequestMapping(value="/update-todo", method = RequestMethod.GET)
    public String showUpdateTodo(@RequestParam int id, ModelMap model){
        Todo todo = service.retrieveTodo(id);
        model.put("todo", todo);
        return "todo";
    }
}
