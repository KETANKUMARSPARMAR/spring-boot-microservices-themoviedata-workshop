package io.javadestiny.moviecatalogservice.resource;

import io.javadestiny.moviecatalogservice.model.CatalogItem;
import io.javadestiny.moviecatalogservice.model.Movie;
import io.javadestiny.moviecatalogservice.model.Rating;
import io.javadestiny.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userid}")
    public List<CatalogItem> getCatalog(@PathVariable String userid){
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userid,
                UserRating.class);
        return ratings.getRatings().stream().map(rating ->{
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(),movie.getDescription(),rating.getRating());
                    }).collect(Collectors.toList());
    }
}
/*Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://127.0.0.1:8082/movies/"+rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();*/