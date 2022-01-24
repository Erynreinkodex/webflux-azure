package com.reinkodex.webfluxazure.gameapi.reactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class GameEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(GameHandler handler) {
        return route(i(GET("/api/v1/reactive/game")), handler::findAll)
                .andRoute(i(GET("/api/v1/reactive/game/{id}")), handler::findById)
                .andRoute(i(POST("/api/v1/reactive/game")), handler::create)
                .andRoute(i(PUT("/api/v1/reactive/game/{id}/declarewinner/{winner}")), handler::declareWinner);
    }

    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}

class CaseInsensitiveRequestPredicate implements RequestPredicate {

    private final RequestPredicate target;

    CaseInsensitiveRequestPredicate(RequestPredicate target) {
        this.target = target;
    }

    @Override
    public boolean test(ServerRequest request) { // <1>
        return this.target.test(new LowerCaseUriServerRequestWrapper(request));
    }

    @Override
    public String toString() {
        return this.target.toString();
    }
}

// <2>
class LowerCaseUriServerRequestWrapper extends ServerRequestWrapper {

    LowerCaseUriServerRequestWrapper(ServerRequest delegate) {
        super(delegate);
    }

    @Override
    public URI uri() {
        return URI.create(super.uri().toString().toLowerCase());
    }

    @Override
    public String path() {
        return uri().getRawPath();
    }

    @Override
    public PathContainer pathContainer() {
        return PathContainer.parsePath(path());
    }
}
