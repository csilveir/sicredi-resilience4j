package br.com.coffeeandit.resilience4j.domain;

import br.com.coffeeandit.resilience4j.repository.GitRepositoryMongo;
import br.com.coffeeandit.resilience4j.dto.GitRepositoryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
@Slf4j
public class GitRepositoryDomain {


    public GitRepositoryDomain(GitHubRepositoryService gitHubRepositoryService, GitRepositoryMongo gitRepositoryMongo) {
        this.gitHubRepositoryService = gitHubRepositoryService;
        this.gitRepositoryMongo = gitRepositoryMongo;
    }


    public Flux<GitRepositoryDTO> mongoGitRepositories(final String username, final Exception exception) {
        log.error("Fallback de domínio para a busca do usuario {} : {}", username, exception.getMessage());
        return Flux.fromIterable(gitRepositoryMongo.findByUsername(username));
    }

    @CircuitBreaker(name = "github", fallbackMethod = "mongoGitRepositories")
    @TimeLimiter(name = "githubTimelimiter", fallbackMethod = "mongoGitRepositories")
    public Flux<GitRepositoryDTO> findGitRepositoriesByUsername(final String username) {
        return gitHubRepositoryService.getGitHubRepositories(username)
                .publishOn(Schedulers.boundedElastic()).map(gitRepositoryDTO -> {
                    return saveLocal(gitRepositoryDTO, username);
                });

    }

    private GitRepositoryDTO saveLocal(final GitRepositoryDTO gitRepositoryDTO, final String username) {
        Optional<GitRepositoryDTO> gitRepositoryDTOOptional = gitRepositoryMongo.findById(gitRepositoryDTO.getId());
        if (gitRepositoryDTOOptional.isEmpty()) {
            gitRepositoryDTO.setUsername(username);
            return gitRepositoryMongo.save(gitRepositoryDTO);
        }
        return gitRepositoryDTOOptional.get();
    }

    public GitRepositoryDTO emptyRepository(final GitRepositoryDTO gitRepositoryDTO, final String username, final Exception exception) {
        log.error("Fallback de Mongo para a busca do usuario {} : {}", username, exception.getMessage());
        return gitRepositoryDTO;
    }

    private final GitHubRepositoryService gitHubRepositoryService;
    private final GitRepositoryMongo gitRepositoryMongo;
}


