package com.demo.reactiverogramming.router;

import com.demo.reactiverogramming.constants.ItemConstant;
import com.demo.reactiverogramming.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.demo.reactiverogramming.constants.ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler){

        return RouterFunctions
                .route(GET(ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                        ,itemsHandler::getAllItems)
                .andRoute(GET(ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::getOneItem)
                .andRoute(POST(ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::createItem)
                .andRoute(DELETE(ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::deleteItem)
                .andRoute(PUT(ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::updateItem);
    }


}
