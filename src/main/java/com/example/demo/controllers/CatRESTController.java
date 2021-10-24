package com.example.demo.controllers;

import com.example.demo.pojos.Cat;
import org.springframework.web.bind.annotation.*;

@RestController
public class CatRESTController {

    @RequestMapping(method = RequestMethod.POST, value = "/cat")
    private Cat getCat(@RequestBody Cat cat) {
        return cat;
    }
}
