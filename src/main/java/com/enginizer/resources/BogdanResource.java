package com.enginizer.resources;


import com.enginizer.model.Role;
import com.enginizer.model.dto.BogdanDTO;
import com.enginizer.model.entities.Bogdan;
import com.enginizer.model.entities.User;
import com.enginizer.repository.BogdanRepository;
import com.enginizer.repository.UserRepository;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by badmotherfucker on 1/21/17.
 */

@RestController

public class BogdanResource {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BogdanRepository bogdanRepository;
    @RequestMapping(value = "/bogdan", method = RequestMethod.GET)
    public ResponseEntity<String> bogdan () {

        return ResponseEntity.ok("hahah");
    }

    @RequestMapping(value = "/bog", method = RequestMethod.POST)
    public ResponseEntity<String> createBogdan(@RequestBody BogdanDTO bogdan){
        Bogdan createdBogdan = new Bogdan();

        createdBogdan.setUsername(bogdan.getBogdanName());
        createdBogdan.setPassword(bogdan.getBogdanPassword());
        createdBogdan.setRole(bogdan.getBogdanRole().getRole());
        Bogdan saved = bogdanRepository.save(createdBogdan);
        return ResponseEntity.ok(saved.getId() + "");
    }
    @RequestMapping(value = "/allbog", method = RequestMethod.GET)
    public @ResponseBody List<Bogdan> allBogdans() {
        List<Bogdan> all = (List<Bogdan>) bogdanRepository.findAll();

        return all;
    }


}
