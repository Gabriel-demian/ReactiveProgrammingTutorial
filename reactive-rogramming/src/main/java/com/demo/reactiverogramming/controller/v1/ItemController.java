package com.demo.reactiverogramming.controller.v1;

import com.demo.reactiverogramming.document.Item;
import com.demo.reactiverogramming.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.demo.reactiverogramming.constants.ItemConstant.ITEM_END_POINT_V1;

@Slf4j
@RestController
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItems(){

        return itemReactiveRepository.findAll();

    }

}
