package br.com.sicredi.sicrediresilience4j.domain;

import br.com.sicredi.sicrediresilience4j.dto.GitRepositoryDTO;
import br.com.sicredi.sicrediresilience4j.repository.GitRepositoryMongo;
import feign.FeignException;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
public class GitHubRepositoryService {


    @Value("${twitter.url}")
    private String url;


    private final GitRepositoryMongo gitRepositoryMongo;

    public GitHubRepositoryService(GitRepositoryMongo gitRepositoryMongo) {
        this.gitRepositoryMongo = gitRepositoryMongo;
    }

    public Flux<GitRepositoryDTO> getGitHubRepositories(final String username) {
        var repositoryUrl = this.url + "/users/" + username + "/repos";
        var githubClient =
                Resilience4jFeign.builder(feignDecorator(username))
                        .encoder(new JacksonEncoder())
                        .decoder(new JacksonDecoder())
                        .target(GithubClient.class, repositoryUrl);
        log.info("Chamando a url {}", repositoryUrl);
        return Flux.fromIterable(githubClient.get());

    }

    private List<GitRepositoryDTO> getByUsername(final String username) {
        return gitRepositoryMongo.findByUsername(username);
    }

    private GithubClient fallback(final String username) {
        return new GithubClient() {
            @Override
            public List<GitRepositoryDTO> get() {
                log.error("Fallback OpenFeign para a busca do usuario {} ", username);

                return getByUsername(username);
            }
        };
    }


    private FeignDecorators feignDecorator(final String username) {
        return FeignDecorators.builder()
                .withFallback(fallback(username), exception -> {
                    log.error(exception.getMessage());
                    return exception instanceof FeignException;
                })
                .build();
    }

}


