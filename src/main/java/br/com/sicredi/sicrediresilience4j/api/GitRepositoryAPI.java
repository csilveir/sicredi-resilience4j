package br.com.sicredi.sicrediresilience4j.api;

import br.com.sicredi.sicrediresilience4j.domain.GitRepositoryDomain;
import br.com.sicredi.sicrediresilience4j.dto.GitRepositoryDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class GitRepositoryAPI {


    public GitRepositoryAPI(GitRepositoryDomain gitRepositoryDomain) {
        this.gitRepositoryDomain = gitRepositoryDomain;
    }

    private GitRepositoryDomain gitRepositoryDomain;

    @GetMapping(value = "/source/repos/{username}")
    public Flux<GitRepositoryDTO> get(@PathVariable final String username) throws InterruptedException {

        return gitRepositoryDomain.findGitRepositoriesByUsername(username);
    }

}
